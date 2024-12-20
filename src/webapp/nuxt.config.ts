// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    compatibilityDate: '2024-04-03',
    devtools: {enabled: false},
    ssr: false,

    runtimeConfig: {
        public: {
            furhat: {
                host: 'localhost',
                port: 8080
            }
        }
    },

    modules: ['@nuxtjs/tailwindcss', '@vueuse/nuxt', "@nuxt/icon", "@formkit/auto-animate/nuxt"],
    icon: {
        serverBundle: "remote"
    }
})