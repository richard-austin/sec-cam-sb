package com.server.controllers

import com.server.commands.SetUpWifiCommand
import com.server.commands.SetupWifiCommand2
import com.server.validators.custom.SetupWifiValidator
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.BindingResult
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ExceptionHandler

//import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import com.server.interfacebjects.ObjectCommandResponse

@RestController
class WifiUtilsController {
//    ValidationErrorService validationErrorService
//    LogService logService
//    WifiUtilsService wifiUtilsService

//    @PostMapping('wifiutils')
//    @Secured(['ROLE_CLOUD', 'ROLE_CLIENT'])
//    ResponseEntity<String> scanWifi() {
//        response.contentType = "application/json"
//        ObjectCommandResponse result = wifiUtilsService.scanWifi()
//        if (result.status == PassFail.PASS) {
//            return ResponseEntity.ok(result.responseObject)
//            return ResponseEntity.ok(result.responseObject)
//        } else
//            return ResponseEntity.internalServerError(result.error)
//
//    }
//

    @GetMapping("/")
    String index() {
        return "Greetings from Spring Boot!";
    }

    @Autowired
    SetupWifiValidator setupWifiValidator

    // @Secured(['ROLE_CLOUD', 'ROLE_CLIENT'])
    @RequestMapping('setUpWifi')
    ResponseEntity<?> setUpWifi(@RequestBody final SetUpWifiCommand cmd) {
        ObjectCommandResponse result
        Errors errors = new BeanPropertyBindingResult(cmd, "setupwifi");
        setupWifiValidator.validate(cmd, errors);
        if (errors.hasErrors()) {
            errors.allErrors.forEach {
                System.out.println(it)
            }
//            def errorsMap = validationErrorService.commandErrors(cmd.errors as ValidationErrors, 'setUpWifi')
//            logService.cam.error "setUpWifi: Validation error: " + errorsMap.toString()
//            render(status: 400, text: errorsMap as JSON)
            return ResponseEntity.badRequest().body(errors.getAllErrors())
        } else {
//            result = wifiUtilsService.setUpWifi(cmd)
//
//            if (result.status == PassFail.PASS) {
//                render(status: 200, text: result.responseObject)
//            } else {
//                logService.cam.error "setUpWifi: error: ${result.error}"
//                result.status = PassFail.FAIL
//                render(status: result.errno, text: result.responseObject as JSON)
//            }
           return ResponseEntity.ok('It Worked!')
        }
    }

//    @Secured(['ROLE_CLOUD', 'ROLE_CLIENT'])
    def checkWifiStatus() {
        ObjectCommandResponse result = wifiUtilsService.checkWifiStatus()
        if (result.status == PassFail.PASS) {
            render(status: 200, text: result.responseObject)
        } else {
            logService.cam.error "checkWifiStatus: error: ${result.error}"
            result.status = PassFail.FAIL
            render(status: 500, text: result.error)
        }
    }

    @PostMapping("/setupWifi2")
    ResponseEntity<?> setupWifi2(@Valid @RequestBody SetupWifiCommand2 cmd) {
//        if(result.hasErrors()) {
//            System.out.println("There are errors")
//            return ResponseEntity.badRequest()
//        }
//        else
            return ResponseEntity.ok("Wifi settings are valid")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    // The audio websocket listener
    boolean started = false
    int count = 0
    OutputStream os
    boolean done = false
    @MessageMapping(value = "/audio")
    protected def audio(@Payload byte[] data) {
        if(!done) {
            if (!started) {
                os = new FileOutputStream("/home/richard/soundfile.bin")
                started = true
            }
            if (++count < 1000)
                os.write(data)
            else {
                os.close()
                done = true
            }
        }
        else
            logService.cam.info("Audio message of ${data.length} bytes received")
    }


//    @Secured(['ROLE_CLOUD', 'ROLE_CLIENT'])
//    def setWifiStatus(SetWifiStatusCommand cmd) {
//        ObjectCommandResponse result = wifiUtilsService.setWifiStatus(cmd)
//        if (result.status == PassFail.PASS) {
//            render(status: 200, text: result.responseObject)
//        } else {
//            logService.cam.error "setWifiStatus: error: ${result.error}"
//            result.status = PassFail.FAIL
//            render(status: result.errno, text: result.error)
//        }
//    }

//    @Secured(['ROLE_CLOUD', 'ROLE_CLIENT'])
//    def getCurrentWifiConnection()
//    {
//        ObjectCommandResponse result = wifiUtilsService.getCurrentWifiConnection()
//
//        if(result.status == PassFail.PASS)
//            render (status: 200, result.responseObject as JSON)
//        else
//        {
//            logService.cam.error "getCurrentWifiConnection: error: ${result.error}"
//            result.status = PassFail.FAIL
//            render(status: 500, text: result.error)
//        }
//    }
//
//    @Secured(['ROLE_CLOUD', 'ROLE_CLIENT'])
//    def getActiveIPAddresses() {
//        ObjectCommandResponse result = wifiUtilsService.getActiveIPAddresses()
//
//        if (result.status == PassFail.PASS)
//            render(status: 200, text: result as JSON)
//        else {
//            logService.cam.error "checkWifiStatus: error: ${result.error}"
//            render(status: 500, text: result.error)
//        }
//    }
//
//    @Secured(['ROLE_CLOUD'])
//    def checkConnectedThroughEthernet() {
//        ObjectCommandResponse result = wifiUtilsService.checkConnectedThroughEthernet()
//        if (result.status == PassFail.PASS) {
//            render(status: 200, text: result.responseObject as JSON)
//        } else {
//            def errMsg = "An error occurred in checkConnectedThroughEthernet:- (${result.error})"
//            logService.cam.error(errMsg)
//            render(status: 500, text: errMsg)
//        }
//    }
//
//    @Secured(['ROLE_CLIENT'])
//    def checkConnectedThroughEthernetNVR() {
//        ObjectCommandResponse result = wifiUtilsService.checkConnectedThroughEthernet(false)
//        if (result.status == PassFail.PASS) {
//            render(status: 200, text: result.responseObject as JSON)
//        } else {
//            def errMsg = "An error occurred in checkConnectedThroughEthernetNVR:- (${result.error})"
//            logService.cam.error(errMsg)
//            render(status: 500, text: errMsg)
//        }
//    }
}
