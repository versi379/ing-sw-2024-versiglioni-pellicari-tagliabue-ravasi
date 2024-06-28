# Codex Naturalis Board Game

<img src="https://www.craniocreations.it/storage/media/products/19/41/Codex_scatola+ombra.png" width=170px height=170px align="center" />

Codex Naturalis Board Game is the final project for the **Software Engineering** course of the **Computer Science Engineering** degree at Politecnico di Milano (A.Y. 2023/2024).

## Project Specifications
This project consists of a Java version of the board game [Codex Naturalis](https://www.craniocreations.it/prodotto/codex-naturalis), by *Cranio Creations*.

The final version includes:
* Initial UML diagram;
* Final (code-generated) UML diagram;
* Working game implementation (rules compliant);
* Implementation source code;
* Unit testing source code.

## Implemented Functionalities
| Functionality | Status |
|:-----------------------|:------:|
| Basic Rules |   ✅    |
| Complete Rules |   ✅    |
| Socket |   ✅    |
| RMI |   ✅    |
| CLI |   ✅    |
| GUI |   ✅    |
| Chat |   ✅    |
| Persistence |    ⛔    |
| Multiple Games |   ✅    |
| Disconnection Resilience |   ⛔    |

## Usage
**Execute Game (Windows, MacOS, Linux)**

Clone the repo
```bash
git clone https://github.com/versi379/ing-sw-2024-versiglioni-pellicari-tagliabue-ravasi.git
```
Move into the newly generated folder
```bash
cd /ing-sw-2024-versiglioni-pellicari-tagliabue-ravasi/deliverables/final/jar
```
Launch server instance
```bash
java -jar appServer.jar
```
Launch client(s) instance(s)
```bash
java -jar appClient.jar
```

**Compile Game (IntelliJ IDEA)**

Compile through Maven.

## Testing
| Package | Class Coverage | Method Coverage | Line Coverage
|:-------------------|:-----------------------------:|:-----------------------------:|:-----------------------------:|
| Model | 100% (33/33) | 100% (237/237) | 99% (874/880)
| Controller | 100% (1/1) | 100% (21/21) | 93% (93/99)

## Software Used
**IntelliJ IDEA Ultimate** - IDE

## Copyright and License

Codex Naturalis is copyrighted.
