import Furhat from "furhat-core";

export default defineNuxtPlugin(async () => {
    const {public: {furhat: {host, port}}} = useRuntimeConfig();
    const _furhat = new Furhat(host, port, "api")

    try {
        await _furhat.init()
    } catch (e) {
        throw createError({
            message: "Non è stato possibile connettersi a Furhat",
            statusCode: 500
        })
    }


    return {
        provide: {
            furhat: _furhat
        }
    }
})