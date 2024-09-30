package com.server.commands

import com.server.validators.custom.IsBoolean
import jakarta.validation.constraints.NotBlank

class SetupWifiCommand2 {

//    SetUpWifiCommand2() {
//
//    }

    @NotBlank
    String ssid

    @NotBlank
    String password

    @IsBoolean
    Boolean isEnabled

    String getSsid() {
        return ssid
    }

    String getPassword() {
        return password
    }

//    void setSsid(String value) {
//        ssid = value
//    }
//
//    void setPassword(String value) {
//        password = value
//    }
//
//    void setIsEnabled(boolean value) {
//        isEnabled = value
//    }

    boolean GetIsEnabled() {
        return isEnabled;
    }

}
