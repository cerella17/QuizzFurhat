import React, {Component} from 'react'

class PlayerIndicators extends Component {
    getIcon = (present) => {
        return present ? "img/UserActive.png" : "img/UserNotActive.png"
    }

    render() {
        const { left, right } = this.props.players
        return (
            <div id="plar_icons">
                <img src={ this.getIcon(left) } id="leftInfo" />
                <img src={ this.getIcon(right) } id="rightInfo" />
            </div>
        )
    }
}

export default PlayerIndicators
