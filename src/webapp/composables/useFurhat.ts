export default function useFurhat() {
    const furhat = useNuxtApp().$furhat;
    return {
        subscribe: <T extends keyof IEvents>(event: T, cb: TEventFn<IEvents[T] & IBaseEvent>) =>
            furhat.subscribe(`furhatos.app.quiz.events.${event}`, cb as any),
    }
}

interface IEvents {
    NewGameEvent: {
        www: string
    }
}

interface IBaseEvent {
    event_id: string
    event_name: string
    event_sessionId: string
    event_time: string
}

type TEventFn<T extends IBaseEvent> = (event: T) => void

