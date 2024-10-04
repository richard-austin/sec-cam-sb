package com.server.controllers

import com.server.commands.SetUpWifiCommand
import com.server.commands.SetupWifiCommand2
import com.server.interfacebjects.Greeting
import com.server.interfacebjects.HelloMessage
import com.server.services.TestService
import com.server.validators.custom.SetupWifiValidator
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.HtmlUtils
import com.server.interfacebjects.ObjectCommandResponse

@RestController
class WifiUtilsController {
// Commented out in favour of index.html in resources/static
//    @GetMapping("/")
//    String index() {
//        return "Greetings from Spring Boot!";
//    }

    @Autowired
    SetupWifiValidator setupWifiValidator
    @Autowired
    TestService testService
    @Autowired
    SimpMessagingTemplate brokerMessagingTemplate

    // @Secured(['ROLE_CLOUD', 'ROLE_CLIENT'])
    @RequestMapping('setUpWifi')
    ResponseEntity<?> setUpWifi(@RequestBody final SetUpWifiCommand cmd) {
        System.out.println(testService.infoFromService)
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
    @Secured("ROLE_CLIENT")
    ResponseEntity<?> setupWifi2(@Valid @RequestBody SetupWifiCommand2 cmd) {
//        if(result.hasErrors()) {
//            System.out.println("There are errors")
//            return ResponseEntity.badRequest()
//        }
//        else
            brokerMessagingTemplate.convertAndSend("/topic/greetings", new Greeting("This is the payload"))
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

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        //Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

}
