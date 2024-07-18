import React, {Component} from 'react'
import { connect } from "react-redux"
import * as Actions from "../actions"
import Menu from '../components/Menu'
import Deck from '../components/Deck'
import DeckSelector from '../components/DeckSelector'
import CallToActionButton from '../components/CallToActionButton'
import PlayerIndicators from '../components/PlayerIndicators'
import FurhatConnection from '../FurhatConnection'
import {bindActionCreators} from "redux";

class App extends Component {
    constructor(props){
        super(props)
    }

    componentDidMount() {
        this.props.actions.updateWindowDimensions();
        window.addEventListener('resize', () => this.props.actions.updateWindowDimensions());
        FurhatConnection(this, this.props.actions)
    }

    componentWillUnmount() {
        window.removeEventListener('resize', () => this.props.actions.updateWindowDimensions())
    }

    selectDeck = (deck, position) => {
        this.sendEvent("SenseSelectDeck", deck.name)
    }

    sendEventExt = (name, params) => {
        console.log("Trying to send an event before Furhat connection is established")
        console.log("Event name " + name + ", params: " + params)
    }

    sendEvent = (name, params) => {
        this.sendEventExt(name, params)
    }

    resetGame = () => {
        this.sendEvent("SenseGameReset", {});
    }

    newGame = () => {
        this.props.actions.showNewGameButton(false)
        this.sendEvent("SenseGameNewGame", {});
    }

    pauseGame = () => {
        this.sendEvent("SenseGamePause", {});
    }

    handleButtonClick = () => {
        this.sendEvent("SenseButtonClick", {});
    }

    sendNewCardLocations = () => {
        const { content, actions } = this.props
        if (!content.deck.synced) {
            // Let our local store know we've synced cards
            actions.deckSynced()

            // Send sync message to backend
            this.sendEvent("SenseCardsMoved", {
                // Backend wants locations as Double
                cards: content.deck.cards.map((card) => {
                    return {
                        ...card,
                        position: {
                            x: card.position.x / this.props.window.width,
                            y: card.position.y / this.props.window.height
                        }
                    }
                })
            })
        }
    }

    getBoard = () => {
        const { content, settings, actions, round, window } = this.props

        return content.deck && content.deck.cards && content.deck.cards.length != 0 ?
            <Deck
                header={content.deck.instruction}
                cards={content.deck.cards}
                revealed ={round.revealed}
                revealDelay={settings.revealDelay}
                truths={content.truths}
                displayIndicators={round.showSolutionIndicators}
                updateInterval={settings.updateInterval}
                handleNewPosition={actions.handleNewPosition}
                sendNewCardLocations={this.sendNewCardLocations}
                draggable={round.draggable}
                window={window} />
            : <DeckSelector
                decks={content.decks}
                selectDeck={this.selectDeck} />
    }

    render() {
      return (
        <div>
            <PlayerIndicators players={this.props.players}/>

            <Menu pauseGame={this.pauseGame} resetGame={this.resetGame} />

            { this.getBoard() }

            <CallToActionButton label={this.props.round.buttonLabel} visible={this.props.round.buttonVisible} action={this.handleButtonClick} />

            <div style={{ position: "absolute", width: 0, height:0, left: "25vw", top: "50vh", visibility: "hidden"}} item-id="game_lowest" />
            <div style={{ position: "absolute", width: 0, height:0, left: "75vw", top: "50vh", visibility: "hidden"}} item-id="game_highest" />

            <img src="/img/logo_dark.png" width="250px" style={{position: "absolute", right: "50px", bottom: "50px"}}/>
        </div>
      )
    }
}

function mapStateToProps(state) {
    return {
        content: state.content,
        players: state.players,
        round: state.round,
        window: state.window,
        settings: state.settings
    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators(Actions, dispatch)
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(App)
