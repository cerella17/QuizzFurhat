import React, {Component} from 'react'

class MenuButton extends Component {
    render() {
        const { action, label } = this.props
        return (
            <button type="button" className={`btn ${label}`} onClick={ action }>
                <i className="fa fa-arrows-alt" aria-hidden="true" style={{ display: "block", fontSize: "24px", marginBottom: "5px" }}></i>
            </button>
        )
    }
}

export default MenuButton

