<template>
  <v-container fluid fill-height>
    <!-- https://github.com/vuetifyjs/vuetify/issues/9773 -->
    <!-- TODO: Remove style="max-width: initial" when this bug is fixed -->
    <v-row align="center" justify="center" style="max-width: initial">
      <v-col cols="12" sm="8" md="4">
        <v-card class="elevation-12">
          <v-toolbar color="primary" dark flat>
            <v-toolbar-title>Gradebook account</v-toolbar-title>
          </v-toolbar>

          <v-form
            @submit.prevent="login"
            ref="form"
            v-model="formValid"
            lazy-validation
          >
            <v-card-text>
              <v-text-field
                v-model="email"
                label="E-mail"
                type="email"
                prepend-icon="mdi-email"
                :rules="emailRules"
              />

              <v-text-field
                v-model="password"
                label="Password"
                :type="showPassword ? 'text' : 'password'"
                prepend-icon="mdi-lock"
                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append="showPassword = !showPassword"
                :rules="passwordRules"
              />
            </v-card-text>

            <v-card-actions>
              <v-spacer />
              <v-btn
                type="submit"
                color="primary"
                :disabled="!formValid"
                :loading="showSpinner"
              >
                Login
              </v-btn>
            </v-card-actions>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
    <v-snackbar color="error" top v-model="showSnackbar">
      {{ snackbarText }}
      <v-btn color="black" text @click="showSnackbar = false">Close</v-btn>
    </v-snackbar>
  </v-container>
</template>

<script>
export default {
  name: "login",

  data() {
    return {
      email: "",
      password: "",

      formValid: true,

      showPassword: false,
      showSpinner: false,

      showSnackbar: false,
      snackbarText: "",

      emailRules: [
        v => !!v || "E-mail is required",
        v => /.+@.+\..+/.test(v) || "E-mail must be valid"
      ],
      passwordRules: [v => !!v || "Password is required"]
    };
  },

  methods: {
    async login() {
      if (!this.$refs.form.validate()) {
        return;
      }

      this.showSpinner = true;
      this.showSnackbar = false;

      const { email, password } = this;

      try {
        await this.$store.dispatch("auth_login", { email, password });
        const redirect = this.$route.query.redirect;

        if (redirect) {
          this.$router.push(redirect);
        } else {
          this.$router.push("/");
        }
      } catch (error) {
        if (error.status === 401) {
          this.snackbarText = `Invalid email or password!`;
        } else if (error.response.statusText) {
          this.snackbarText = `Server error: ${error.response.statusText}`;
        } else {
          this.snackbarText = "Unknown error!";
        }

        this.showSnackbar = true;
      }

      this.showSpinner = false;
    }
  }
};
</script>
