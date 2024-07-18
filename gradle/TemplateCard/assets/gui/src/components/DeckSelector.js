import React, {Component} from 'react'
import ReactDOM from "react-dom"
import LoadingIndicator from "./LoadingIndicator";

class _Deck extends Component {
    constructor(props) {
        super(props)
        this.state = {
            x: 0
        }
    }

    componentDidMount() {
        var n = ReactDOM.findDOMNode(this);
        this.setState({
            x: n.offsetLeft
        })
    }

    render() {
        const { deck, selectDeck, y } = this.props
        return (
            <div className="category" onClick={ () => selectDeck(deck, {x: this.state.x, y: y}) }>
                <div className="picture" style={{ backgroundImage: `url(${deck.imageSrc})` }} />
                <div className="label">{deck.name}</div>
            </div>
        )
    }
}

class DeckSelector extends Component {
    constructor(props){
        super(props)
        this.state = {
            y: 0
        }
    }

    componentDidMount() {
        var n = ReactDOM.findDOMNode(this);
        this.setState({
            y: n.offsetTop
        })
    }

    getDecks = () => {
        const { decks, selectDeck } = this.props
        if (decks.length == 0) {
            //return <LoadingIndicator />
            return <div></div>
        }
        else {
            return decks.map((deck) =>
                <_Deck key={deck.name} deck={deck} y={this.state.y} selectDeck={selectDeck}/>
            )
        }
    }

    render() {
        return (
            <div className="categoryset" id="categoryset">
                { this.getDecks() }
            </div>
        )
    }
}

export default DeckSelector
