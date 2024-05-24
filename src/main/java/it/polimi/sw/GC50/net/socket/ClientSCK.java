package it.polimi.sw.GC50.net.socket;

/*
public class ClientSCK implements Runnable {
    private View view;
    private final int port;
    private final String address;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private ArrayList<String> freeMatch;
    private GameStatus gameStatus;
    //match
    private String matchName;
    private String nickName;
    private GameView gameView;
    //private ModelMex modelMex;
    ///////////////////////////////////////////
    private boolean alive;
    private boolean allPlayerReady;
    private boolean myTurn;
    private boolean error;
    private boolean statusGame;
    ///////////////////////////////////////////
    //Thread
    Thread thread2;
    Thread thread1;
    private Thread thread3;
    private Thread thread4;
    private final ExecutorService executorService;
    /////////////////////////////////////////////////////
    private Object[] lock;
    private Queue<Message1> queue;
    private boolean[] condition;

    public ClientSCK(int port, String address) throws IOException {
        this.port = port;
        this.address = address;
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(address, port), 1000);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());

        System.out.println("connected to the server");
        this.view = null;
        this.myTurn = false;
        this.alive = true;
        this.matchName = null;
        this.nickName = null;
        this.freeMatch = new ArrayList<>();


        this.gameStatus = GameStatus.SETUP;

        this.executorService = Executors.newSingleThreadScheduledExecutor();

        this.queue = new LinkedList<>();

        this.condition = new boolean[6];
        this.lock = new Object[6];
        for (int i = 0; i < 6; i++) {
            this.condition[i] = true;
            this.lock[i] = new Object();
        }
        this.error = false;
        this.statusGame = false;
        this.allPlayerReady = false;

    }

    //////////////////////////////////////////
    //COMUNICATION WITH SERVER
    ///////////////////////////////////////////

    private void inputThread() {

        executorService.execute(() -> {
            while (!executorService.isShutdown()) {
                try {
                    Object object = input.readObject();
                    Message1 message1 = (Message1) object;
                    queue.add(message1);
                    notifyMessageFromServer();
                    //System.out.println((boolean) message.getObject());


                } catch (IOException | ClassNotFoundException e) {

                }

            }
        });

    }

    private synchronized void setMessageout(Message1.Message1ClientToServer messageout) {
        try {
            output.writeObject(messageout);
            output.flush();
            output.reset();
        } catch (IOException e) {

        }
    }

    private void switchmex() {
        while (!queue.isEmpty()) {
            switchmex(queue.poll());
        }
    }

    private void switchmex(Message1 mex) {
        switch (mex.getRequest()) {
            case SET_NAME_RESPONSE: {
                boolean response = (boolean) mex.getObject();
                if (!response) {
                    nickName = null;
                }
                System.out.println("name setted");
                notifyFirsPhase();
                break;
            }
            case GET_FREE_MATCH_RESPONSE: {
                freeMatch = (ArrayList<String>) mex.getObject();
                notifyFirsPhase();
                break;
            }
            case CREATE_GAME_RESPONSE, ENTER_GAME_RESPONSE: {
                boolean response = (boolean) mex.getObject();
                if (!response) {
                    this.matchName = null;
                }
                notifyFirsPhase();
                break;
            }
            case NOTIFY_GAME_SETUP: {
                allPlayerReady = true;
                notifyMidPhase();
                break;
            }

            case NOTIFY_CARD_PLACED, NOTIFY_NEXT_TURN, NOTIFY_CARD_DRAWN: {
                if (GameStatus.PLAYING.equals(gameStatus)) {
                    error = false;
                    notifyMidPhase();
                }
                break;
            }
            case NOTIFY_PLAYER_JOINED_GAME: {
                break;
            }
            case NOTIFY_PLAYER_LEFT_GAME: {
                break;
            }
            case NOTIFY_PLAYER_READY: {
                break;
            }

            case NOTIFY_GAME_STARTED: {
                this.gameStatus = GameStatus.PLAYING;
                this.statusGame = true;
                error = false;
                notifyMidPhase();
                break;
            }
            case NOTIFY_CHAT_MESSAGE: {
                if (!view.getClass().getSimpleName().equals("TuiView")) {
                    view.updateChat((Chat) mex.getObject());
                }
                break;

            }
            case NOTIFY_ERROR: {
                if (mex.getObject().equals(nickName)) {
                    error = true;
                    notifyMidPhase();
                }
                break;
            }
            case GET_MODEL_RESPONSE: {
                this.gameView = (GameView) mex.getObject();
                notifyModelChangedFromServer();
                break;
            }

            default: {
                break;
            }
        }


    }


    private void lock(int index) {
        if (index > this.condition.length) {
            return;
        }
        synchronized (this.lock[index]) {
            while (this.condition[index]) {
                try {
                    this.lock[index].wait();
                } catch (InterruptedException e) {
                    System.out.println("error");
                }
            }
        }
        condition[index] = true;
    }

    private void unlock(int index) {
        if (index > this.condition.length) {
            return;
        }
        synchronized (this.lock[index]) {
            this.condition[index] = false;
            this.lock[index].notifyAll();
        }

    }

    private void notifyMessageFromServer() {
        unlock(0);
    }

    private void waitMessageFromServer() {
        lock(0);
    }

    private void notifyChatUpdate() {
        unlock(1);

    }

    private void waitChatUpdate() {
        lock(1);
    }

    private void notifyModelChangedFromServer() {
        unlock(2);
    }

    private void waitModelChangedFromServer() {
        lock(2);
    }

    private void notifyMidPhase() {
        unlock(3);

    }

    private void waitMidPhase() {
        lock(3);
    }

    private void notifyFirsPhase() {
        unlock(4);

    }

    private void waitFirstPhase() {
        lock(4);
    }


//////////////////////////////////////////
//LOBBY
///////////////////////////////////////////


    public void addView(View view) {
        this.view = view;
    }

    private String createGame(String matchName, int numberOfPlayer) {
        this.matchName = matchName;
        setMessageout(new Message1.Message1ClientToServer(Request.CREATE_GAME, numberOfPlayer, matchName, nickName));
        waitFirstPhase();
        return this.matchName;

    }

    private String enterGame(String matchName) {
        this.matchName = matchName;
        setMessageout(new Message1.Message1ClientToServer(Request.ENTER_GAME, null, matchName, nickName));
        waitFirstPhase();
        return this.matchName;
    }

    private String setName(String name) {
        this.nickName = name;
        setMessageout(new Message1.Message1ClientToServer(Request.SET_NAME, null, null, name));
        waitFirstPhase();
        return this.nickName;
    }

    private ArrayList<String> getFreeMatch() {
        setMessageout(new Message1.Message1ClientToServer(Request.GET_FREE_MATCH, null, null, null));
        waitFirstPhase();
        return this.freeMatch;
    }


//////////////////////////////////////////
//ACTIVE GAME
///////////////////////////////////////////


    private void placeCard(PlaceCardRequest placeCardRequest) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message1.Message1ClientToServer(Request.PLACE_CARD, placeCardRequest, this.matchName, this.nickName));
        waitMidPhase();
        if (!error) {
            getModel();
            //view.updateBoard();
        }
        error = false;
    }

    private void sendMessage(String message) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message1.Message1ClientToServer(Request.MEX_CHAT, message, this.matchName, this.nickName));

    }

    private void selectStarterFace(int face) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message1.Message1ClientToServer(Request.SELECT_STARTER_FACE, face, this.matchName, this.nickName));
        waitFirstPhase();
    }


    private void selectObjectiveCard(int index) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message1.Message1ClientToServer(Request.SELECT_OBJECTIVE_CARD, index, this.matchName, this.nickName));
        waitFirstPhase();
    }


    private void drawCard(int position) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message1.Message1ClientToServer(Request.DRAW_CARD, position, this.matchName, this.nickName));
        waitMidPhase();
        if (!error) {
            getModel();
            //view.updateBoard();
        }
        error = false;
    }


    private Object getModel() {
        setMessageout(new Message1.Message1ClientToServer(Request.GET_MODEL, null, this.matchName, this.nickName));
        waitModelChangedFromServer();
        view.setModel(this.gameView);
        // ??? waitNotifyModelChangedFromServer();
        view.setModel(this.gameView);

        if (this.gameView.getCurrentPlayer().equals(this.nickName)) {
            this.myTurn = true;
        } else {
            this.myTurn = false;
        }
        return this.gameView;
    }

    //////////////////////////////////////////
    //LOBBY_CONTROLLER
    ///////////////////////////////////////////

    public void lobby() throws InterruptedException {
        System.out.println("prova");
        while (this.setName(view.selectName()) == null) {
            System.out.println("name not valid");
        }
        System.out.println("username impostato rete |||||||||||||||||||");
        do {
            switch (view.selectJoinOrCreate()) {
                case 1: {
                    while (this.createGame(view.selectGameName(), view.selectNumberOfPlayers()) == null) {
                        System.out.println("game name not valid");
                    }
                    break;
                }
                case 2: {
                    if (this.getFreeMatch() == null || this.freeMatch.isEmpty()) {
                        System.out.println("no free match");
                        break;
                    }
                    System.out.println("free match");
                    for (String s : this.freeMatch) {
                        System.out.println(s);
                    }
                    while (this.enterGame(view.selectGameName()) == null) {
                        System.out.println("game name not valid");
                    }
                    break;
                }
            }
        } while (this.matchName == null);
        waitingPlayer();
    }

    private void waitingPlayer() throws InterruptedException {

        view.showWaitPlayers();
        long startTime = System.currentTimeMillis();

        while (!this.allPlayerReady) {
            try {

                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                if (elapsedTime >= 4 * 60 * 1000) {
                    System.out.println("timeout");
                    break;
                }
                Thread.sleep(11);
            } catch (InterruptedException e) {
                this.lobby();
            }
        }
        if (this.allPlayerReady) {
            view.showSetup();
            firstPhase();
        } else {
            this.lobby();
        }

    }


    //////////////////////////////////////////
    //ACTIVE_GAME_CONTROLLER
    ///////////////////////////////////////////
    private void firstPhase() {
        this.getModel();
        view.setModel(this.gameView);
        this.selectObjectiveCard(view.selectObjectiveCard());
        this.selectStarterFace(view.selectStarterFace());
        midPhase();
    }

    private void midPhase() {

        int y = 41;
        int x = 41;
        waitMidPhase();
        getModel();
        ///////////////////////////////////////////

        ///////////////////////////////////////////
        while (statusGame) {
            if (myTurn) {
                if (gameView.getPlayingPhase().equals(PlayingPhase.DRAWING)) {
                    this.drawCard(view.selectDrawingPosition());
                } else if (gameView.getPlayingPhase().equals(PlayingPhase.PLACING)) {
                    this.placeCard(view.selectPlaceCard());
                }
            } else {
                waitMyTurn();
            }
        }

    }


    private void waitMyTurn() {
        while (!myTurn) {
            waitMidPhase();
            if (!error) {
                getModel();
                //view.updateBoard();
            }
            error = false;
        }
    }

    private void endPhase() {
        this.getModel();
        //view.endGame();
    }

    //////////////////////////////////////////
    //PASSIVE_GAME_CONTROLLER
    ///////////////////////////////////////////

    @Override
    public void run() {
        thread1 = new Thread(() -> {
            inputThread();
        });


        thread2 = new Thread(() -> {
            while (alive) {
                waitMessageFromServer();
                switchmex();
            }
        });
        thread2.start();
        thread1.start();

/*
        thread3 = new Thread(() -> {
            while (alive) {
                sendMessage(view.askMessage());
            }
        });

    }


}




 */