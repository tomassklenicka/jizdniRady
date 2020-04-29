package com.example.jizdnirady.resources;

import com.example.jizdnirady.connection.SqlServerConnect;
import com.example.jizdnirady.models.AuthenticationRequest;
import com.example.jizdnirady.models.AuthenticationResponse;
import com.example.jizdnirady.repository.UserRepository;
import com.example.jizdnirady.services.MyUserDetailsService;
import com.example.jizdnirady.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RestController
public class userResource {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private SqlServerConnect sqlServerConnect;

    static Connection conn = null;
    static PreparedStatement pst = null;
    static ResultSet rs = null;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "hello world";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Wrong Credentials", HttpStatus.FORBIDDEN);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> createAccount(@RequestHeader("username") final String username,
                                           @RequestHeader("password") final String password) throws Exception {
        if (userRepository.findByUserName(username).isPresent()) {
            return new ResponseEntity("userName used", HttpStatus.IM_USED);
        } else {
            String query = "INSERT INTO users (active, password, roles, user_name) values (1,?,'USER_ROLE',?)";

            conn = sqlServerConnect.ConnectDB();
            pst = conn.prepareStatement(query);
            pst.setString(1, password);
            pst.setString(2, username);
            pst.executeUpdate();
        }

        AuthenticationRequest auth = new AuthenticationRequest();
        auth.setUsername(username);
        auth.setPassword(password);
        return createAuthenticationToken(auth);

        // return new ResponseEntity<>("ok",HttpStatus.OK);
    }

    @RequestMapping(value = "/post_event", method = RequestMethod.POST)
    public void postNewEvent(@RequestHeader("Evevnt_Type") String eventType, @RequestHeader("Date") String date) {
        //TODO
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public void getEvents() {
        //TODO
    }

    @RequestMapping(value = "/hledej", method = RequestMethod.GET)
    public ResponseEntity<?> createAccount(
            @RequestHeader("from") final String startStop,
            @RequestHeader("to") final String destinationStop,
            @RequestHeader("time") final String depaertureTime,
            @RequestHeader("number_of_transfers") final String numberOfTransfers) {
        //TODO
        return ResponseEntity.ok(new AuthenticationResponse("blank"));
    }
}
