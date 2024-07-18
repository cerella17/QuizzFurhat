import React, {Component} from 'react'
import MenuButton from './MenuButton'

class Menu extends Component {
    toggleFullScreen = () => {
        if ((document.fullScreenElement && document.fullScreenElement !== null) || (!document.mozFullScreen && !document.webkitIsFullScreen)) {
            if (document.documentElement.requestFullScreen) {
              document.documentElement.requestFullScreen();
            }
            else if (document.documentElement.mozRequestFullScreen) {
                document.documentElement.mozRequestFullScreen();
            }
            else if (document.documentElement.webkitRequestFullScreen) {
                document.documentElement.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
            }
        }
        else {
            if (document.cancelFullScreen) {
                document.cancelFullScreen();
            }
            else if (document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            }
            else if (document.webkitCancelFullScreen) {
                document.webkitCancelFullScreen();
            }
        }
    }


    render() {
        const { resetGame, pauseGame } = this.props
        return (
            <div id="top_menu" style={{ zIndex: 2 }}>
                <MenuButton action={this.toggleFullScreen} label={"Fullscreen"} />
                <MenuButton action={resetGame} label={"Reset"} />
                <MenuButton action={pauseGame} label={"Pause"} />
            </div>
        )
    }
}

export default Menu


