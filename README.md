# Quiz

Quiz realizzato per UNISA ORIENTA

## Description

Il robot Furhat, una volta individuate le persone, spiegherà le regole del gioco e sceglierà due capigruppo ai quali verranno fatte delle domande di cultura generale. La squadra che avrà totalizzato più punti vincerà la partita.

## Usage

Max number of users is set to: 2

### Setup

1. **Install Dependencies**: Make sure you have the Furhat SDK and any other necessary dependencies installed.
2. **Run the Application**: Start the Furhat application and deploy the quiz skill.

### Game Rules

1. **Form Teams**:
    - Il robot individua le persone e chiede di formare due squadre.
    - I capigruppo vengono scelti e identificati dal robot.

2. **Quiz Questions**:
    - Domande di cultura generale vengono poste ai capigruppo.
    - Le risposte corrette aumentano il punteggio della squadra.

3. **Winning the Game**:
    - La squadra con il punteggio più alto alla fine delle domande vince la partita.

### Developer Notes

- **User Variables**: Utilizzate per tenere traccia del punteggio, del nome del capogruppo e dello stato del gioco.
- **States**: Gestione degli stati per controllare il flusso del gioco e la logica delle domande.

### Example Flow

1. **Benvenuto**: Il robot saluta i partecipanti e spiega le regole del gioco.
2. **Formazione delle Squadre**: I partecipanti formano due squadre e vengono scelti i capigruppo.
3. **Inizio del Quiz**: Le domande vengono poste ai capigruppo in alternanza tra le squadre.
4. **Conclusione**: Viene dichiarato il vincitore basato sul punteggio accumulato.

## License

Questo progetto è concesso in licenza sotto i termini della licenza MIT. Per maggiori dettagli, consulta il file LICENSE.

