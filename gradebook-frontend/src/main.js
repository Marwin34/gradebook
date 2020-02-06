import Vue from "vue";
import Vuetify from "vuetify/lib";
import VuePageTransition from "vue-page-transition";

import store from "./store";
import router from "./router";

import App from "./App.vue";

import { configureAxios } from "./jwt";
configureAxios();

Vue.config.productionTip = false;
Vue.use(Vuetify);
Vue.use(VuePageTransition);
const vuetify = new Vuetify({
  theme: {
    dark: window.localStorage.getItem("theme") === "dark" ? true : false
  }
});

(async () => {
  if (store.getters.isAuthenticated) {
    await store.dispatch("current_user");
  }
})().then(
  new Vue({
    router,
    store,
    vuetify,
    render: h => h(App)
  }).$mount("#app")
);
