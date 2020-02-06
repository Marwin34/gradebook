<template>
  <div>
    <v-navigation-drawer v-model="drawer" app>
      <v-container class="pa-1">
        <v-row>
          <v-col class="ma-0" align="center">
            <v-avatar size="100">
              <v-icon size="100">mdi-account-circle</v-icon>
            </v-avatar>
          </v-col>
        </v-row>
        <v-row>
          <v-col align="center" class="pa-0 ma-0">
            <p class="title text-truncate mb-1">
              {{ $store.getters.fullName }}
            </p>
            <p class="font-italic">
              {{ $store.getters.email }}
            </p>
            <p v-if="$store.getters.isAdmin" class="overline primary--text">
              Administrator
            </p>
          </v-col>
        </v-row>
        <v-row>
          <v-divider />
        </v-row>
      </v-container>
      <v-list dense>
        <v-list-item v-for="link in links" :to="link.route" :key="link.name">
          <v-list-item-action>
            <v-icon>{{ link.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>{{ link.name }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <template v-slot:append>
        <v-divider />
        <div class="px-5 py-1">
          <v-row>
            <v-spacer />
            <v-btn @click="changeTheme" icon>
              <v-icon v-if="darkTheme">mdi-brightness-4</v-icon>
              <v-icon v-else>mdi-brightness-7</v-icon>
            </v-btn>
          </v-row>
        </div>
      </template>
    </v-navigation-drawer>
    <v-app-bar app color="primary" dark>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-spacer />
      <v-toolbar-title class="hidden-xs-only">
        <v-icon left>mdi-school</v-icon>
        Gradebook
      </v-toolbar-title>
      <v-spacer />
      <v-btn text @click="logout" :loading="showLogoutSpinner">
        <v-icon left>mdi-logout</v-icon>
        <span>Sign Out</span>
      </v-btn>
    </v-app-bar>
  </div>
</template>

<script>
export default {
  name: "navigation",

  data() {
    return {
      drawer: false,
      showLogoutSpinner: false,

      allLinks: [
        { name: "Home", route: "/", icon: "mdi-home" },
        {
          name: "Account",
          route: "/account",
          icon: "mdi-account"
        },
        {
          name: "Administrator panel",
          route: "/admin",
          icon: "mdi-tools",
          condition: () => this.$store.getters.isAdmin
        },
        {
          name: "Teacher panel",
          route: "/teacher",
          icon: "mdi-teach",
          condition: () => this.$store.getters.isTeacher
        },
        {
          name: "Courses",
          route: "/teacher-courses",
          icon: "mdi-teach",
          condition: () => this.$store.getters.isTeacher
        }
      ]
    };
  },

  computed: {
    links() {
      return this.allLinks.filter(link =>
        link.condition ? link.condition() : true
      );
    },

    darkTheme() {
      return this.$vuetify.theme.dark;
    }
  },

  methods: {
    async logout() {
      this.showLogoutSpinner = true;
      await this.$store.dispatch("auth_logout");
      this.showLogoutSpinner = false;
    },

    changeTheme() {
      let dark = !this.$vuetify.theme.dark;
      this.$vuetify.theme.dark = dark;
      localStorage.setItem("theme", dark ? "dark" : "light");
    }
  }
};
</script>
