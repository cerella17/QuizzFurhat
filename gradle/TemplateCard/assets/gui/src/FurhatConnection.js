import Furhat from 'furhat-gui'

const FurhatConnection = (INSTANCE, actions) => {
    Furhat(function (furhat) {
        furhat.subscribe('furhatos.app.cardgame.ActionShowDecks', function (data) {
            if (data.decks != null) {
                actions.decksReceived(data.decks)
            }
        })

        furhat.subscribe('furhatos.app.cardgame.ActionSettingsUpdate', function (data) {
            if (data != null) {
                actions.settingsUpdated(data)
            }
        })

        furhat.subscribe('furhatos.app.cardgame.ActionDealCards', function (data) {
            if (data.deck != null) {
                actions.deckLoaded(data.deck)
                setTimeout(() => {
                    actions.dealCards()
                }, 100)
            }
        })

        furhat.subscribe('furhatos.app.cardgame.ActionClearTable', function (data) {
            actions.clearTable()
        })

        furhat.subscribe('furhatos.app.cardgame.ActionShowButton', function (data) {
            if (data.label != null) {
                actions.showButton(data.label)
            }
        })

        furhat.subscribe('furhatos.app.cardgame.ActionHideButton', function (data) {
            actions.hideButton()
        })

        furhat.subscribe('furhatos.app.cardgame.ActionGameState', function (data) {
            if (data.decks != null) {
                actions.decksReceived(data.decks)
            }
            if (data.deck != null) {
                actions.deckLoaded(data.deck, true)
            }
            if (data.left != null || data.right != null) {
                actions.playersUpdated(data)
            }
            if (data.button != null && data.button != "") {
                actions.showButton(data.button)
            }
            if (data.indicators != null) {
                actions.showSolutionIndicators(data.indicators)
            }
            if (data.solution != null) {
                actions.revealSolution(data.truths)
            }
        })

        furhat.subscribe('furhatos.app.cardgame.ActionShowSolutionIndicators', function (data) {
            if (data.indicators != null) {
                actions.showSolutionIndicators(data.indicators)
            }
        })

        furhat.subscribe('furhatos.app.cardgame.ActionRevealSolution', function (data) {
            if (data.solution != null) {
                actions.revealSolution(data.solution)
            }
        })

        furhat.subscribe('furhatos.app.cardgame.ActionPlayersUpdate', function (data) {
            if (data.left != null || data.right != null) {
                actions.playersUpdated(data)
            }
        })

        furhat.send({
            event_name: 'furhatos.event.requests.RequestSkillGUIName',
            port: window.location.port,
        })

        INSTANCE.sendEventExt = (name, params) => {
            furhat.send({
                event_name: name,
                params: params
            })
        }
    })
}

export default FurhatConnection
