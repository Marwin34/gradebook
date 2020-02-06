<template>
  <v-app>
    <Navigation v-if="showNavigation" />
    <v-content>
      <router-view v-if="login" />
      <v-container v-else fluid>
        <vue-page-transition name="zoom">
          <router-view />
        </vue-page-transition>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
import Navigation from "./components/Navigation";

export default {
  name: "App",

  components: {
    Navigation
  },

  computed: {
    showNavigation() {
      return this.$store.getters.isAuthenticated;
    },

    login() {
      return this.$route.name == "login";
    }
  }
};
</script>
