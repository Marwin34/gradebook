<template>
  <div>
    <v-row justify="center">
      <v-col cols="10" md="6">
        <v-card class="pa-3">
          <v-form
            @submit.prevent="changePassword"
            ref="changePasswordForm"
            v-model="changePasswordFormValid"
          >
            <v-card-title>Change password</v-card-title>
            <v-card-text>
              <v-text-field
                v-model="currentPassword"
                label="Current password"
                type="password"
                prepend-icon="mdi-lock"
                :rules="currentPasswordRules"
              ></v-text-field>
              <v-text-field
                v-model="newPassword"
                label="New password"
                type="password"
                prepend-icon="mdi-lock-plus"
                :rules="newPasswordRules"
              ></v-text-field>
              <v-card-actions>
                <v-spacer />
                <v-btn
                  type="submit"
                  color="primary"
                  :disabled="!changePasswordFormValid"
                  :loading="showChangePasswordSpinner"
                >
                  Change
                </v-btn>
              </v-card-actions>
            </v-card-text>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
    <v-snackbar :color="snackbarColor" v-model="showSnackbar">
      {{ snackbarText }}
      <v-btn color="black" text @click="showSnackbar = false">Close</v-btn>
    </v-snackbar>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "account",

  data() {
    return {
      showSnackbar: false,
      snackbarColor: "",
      snackbarText: "",

      currentPassword: "",
      newPassword: "",
      changePasswordFormValid: true,
      showChangePasswordSpinner: false,

      currentPasswordRules: [v => !!v || "Current password is required"],
      newPasswordRules: [
        v => !!v || "New password is required",
        v => v.length >= 8 || "Must be at least 8 characters long"
      ]
    };
  },

  methods: {
    async changePassword() {
      if (!this.$refs.changePasswordForm.validate()) {
        return;
      }

      this.showChangePasswordSpinner = true;

      try {
        const { currentPassword, newPassword } = this;
        await axios.post("api/auth/change-password", {
          currentPassword,
          newPassword
        });

        this.snackbarColor = "success";
        this.snackbarText = "Password changed successfully";
        this.showSnackbar = true;
      } catch (error) {
        try {
          this.snackbarText = error.response.data.error.message;
        } catch {
          this.snackbarText = "Unable to change password";
        }

        this.snackbarColor = "error";
        this.showSnackbar = true;
      }

      this.showChangePasswordSpinner = false;
    }
  }
};
</script>
