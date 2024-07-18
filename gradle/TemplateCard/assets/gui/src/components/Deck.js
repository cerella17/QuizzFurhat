import React, {Component} from 'react'
import Card from './Card'

class Deck extends Component {
    constructor(props){
        super(props)

        const card_height = 0.4 // set in CSS
        const card_width = 0.15 // set in CSS
        const { width, height } = props.window

        this.state = {
            highestZ: 1,
            initialPosition: {
                x: width / 2 - card_width * width / 2,
                y: - card_height * height * 2
            },
            card_height,
            card_width
        }
    }

    componentWillMount() {
        // For ongoing games, on refresh, we set currentPositions in a static way here
        this.setState({
            currentPositions: this.props.cards.map((card, index) => card.position)
        })
    }

    componentDidMount() {
        const { revealed, cards, revealDelay, sendNewCardLocations, updateInterval } = this.props

        // For new games, revealed is false and we set a timeout for when the cards should start animate to position
        if (!revealed) {
            setTimeout(() => {
                    this.setState({
                        goalPositions: cards.map((card, index) => {
                            // Get our goalPosition
                            const position = this.calcStartPosition(index)
                            // Announce positions to backend
                            this.props.handleNewPosition(index, position)
                            return position
                        })
                    })
                }, revealDelay
            )
        }

        // Update backend on our positionings on set interval
        this.interval = setInterval(sendNewCardLocations, updateInterval)
    }

    componentWillUnmount() {
        clearInterval(this.interval)
    }

    calcStartPosition = (order, index = null) => {
        const { height, width } = this.props.window
        const { cards, revealed } = this.props
        const { card_width, card_height } = this.state

        const unused_width = (1.0 - cards.length * card_width) * width
        const padding_width = unused_width / (cards.length + 1)

        const x = (order + 1) * padding_width + order * card_width * width
        const y = (1.0 - card_height) * height / 2

        // Since the Draggable card-components use CSS transform-syntax for dragged
        // movements, we need to calculate the offset (i.e the dragged distances)
        // from the animation targets when we don't get the positions from the backend
        // i.e when revealed is true and when we pass in an index (since the first parameter to this function
        // is used to find out the position for the ORDER of the card according to the truth values)
        const offsetX = revealed && index != null ? (cards[index].position.x - this.calcStartPosition(index).x) : 0
        const offsetY = revealed && index != null ? (cards[index].position.y - y) : 0

        const position = {
            x: x - offsetX,
            y: y - offsetY
        }

        return position
    }

    incrementZindex = () => {
        let newZ = this.state.highestZ + 1
        this.setState({
            ...this.state,
            highestZ: newZ
        })
        return newZ
    }

    getGoalPosition = (index) => {
        // If we have a truths array defined, this means that we have already solved the cards
        // in which case we want to animate to the correct order and not to start-positions
        if (this.props.truths.length > 0) {
            const truth = this.props.truths.find((t, i) => i == index)
            return this.calcStartPosition(truth.order, index)
        }
        else {
            // Otherwise, we want to animate to the preset start-positions. Needs to be taken from state on a timeout
            // since the components need to be mounted initially with a position. There are probably work-arounds...
            return this.state.goalPositions ? this.state.goalPositions[index] : false
        }
    }

    getCards = () => {
        const { cards, truths, revealed, handleNewPosition, draggable, displayIndicators } = this.props

        return revealed ? cards.map((card, index) =>
                <Card
                    key={card.name}
                    index={index}
                    name={card.name}
                    imageSrc={card.imageSrc}
                    truth={truths[index] ? truths[index] : false}
                    indicator={card.solutionIndicator}
                    displayIndicator={displayIndicators}
                    handleNewPosition={handleNewPosition}
                    orderChanged={this.orderChanged}
                    draggable={draggable}
                    startPosition={revealed ? this.state.initialPosition : this.state.currentPositions[index]}
                    goalPosition={this.getGoalPosition(index)}
                    incrementZindex={this.incrementZindex}
                />
            )
            : <div></div>
    }

    render() {
        return (
            <div className="cards">
                <h1 className="header1">
                    { this.props.header }
                </h1>

                { this.getCards() }
            </div>
        )
    }
}

export default Deck
