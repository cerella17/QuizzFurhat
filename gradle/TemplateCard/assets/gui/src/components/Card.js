import React, {Component} from 'react'
import Draggable from 'react-draggable'
import ReactDOM from "react-dom"

class Card extends Component {
    constructor(props){
        super(props)
        this.state = {
            zIndex: 1,
            draggable: false,
            lastGoalPosition: {
                x: -1,
                y: -1
            }
        }
    }

    componentDidMount() {
        this.setState({
            ...this.state,
            node: ReactDOM.findDOMNode(this)
        })
    }

    handleDragStart = (e) => {
        this.setState({
            ...this.state,
            zIndex: this.props.incrementZindex(),
        })
    }

    handleDrag = (e) => {
        // We're not really interested in the touch-event but rather where this component is at this point
        const pos = this.state.node.getBoundingClientRect()

        this.props.handleNewPosition(this.props.index, {
            x: pos.x,
            y: pos.y
        })
    }

    handleTransitionEnd = (e) => {
        this.setState({
            ...this.state,
            position: this.props.goalPosition,
            lastGoalPosition: this.props.goalPosition,
        })
    }

    render() {
        const { name, imageSrc, truth, draggable, startPosition, goalPosition, displayIndicator, indicator } = this.props
        const { position, lastGoalPosition } = this.state
        const currentPosition = position ? position : startPosition
        const animating = goalPosition && goalPosition != lastGoalPosition

        return (
            <Draggable
                disabled={!draggable}
                key={name}
                bounds="body"
                onStart={this.handleDragStart}
                onDrag={this.handleDrag}
            >
                <div className={"card " + (animating ? "animate" : "")}
                    style={{
                        zIndex: this.state.zIndex,
                        left: animating ? goalPosition.x : currentPosition.x,
                        top: animating ? goalPosition.y : currentPosition.y,
                     }}
                    onTransitionEnd={this.handleTransitionEnd}
                 >
                    <div id="cardpic1" className="picture" style={{ backgroundImage: `url(${imageSrc})` }}>
                        <img id="cardsmi1" src={ indicator? "img/happy.png" : "img/sad.png" } style={{ display: displayIndicator ? "block" : "none" }} className="smiley"/>
                        <div id="cardtruth1" style={{ display: truth ? "block" : "none" }} className="truthlabel">{ truth.truth } </div>
                    </div>
                    <div id="cardlbl1" className="label">
                        { name }
                    </div>
                </div>
            </Draggable>
        )
    }
}

export default Card
