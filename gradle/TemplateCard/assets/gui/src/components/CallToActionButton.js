import React, {Component} from 'react'

class CallToActionButton extends Component {
    render() {
        const { label, visible, action } = this.props
        return (
            <div style={{ position: "absolute", left: "0px", top: "80vh", width: "100%", textAlign: "center", zIndex: 9999 }}>
                <button
                    id="taskbutton"
                    type="button"
                    onClick={ () => action() }
                    item-id="game_button"
                    style={{ display: visible ? "inherit" : "none", margin: "0 auto" }}
                    >
                    { label }
                </button>
            </div>
        )
    }
}

export default CallToActionButton

