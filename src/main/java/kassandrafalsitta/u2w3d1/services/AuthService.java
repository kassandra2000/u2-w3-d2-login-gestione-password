package kassandrafalsitta.u2w3d1.services;

import kassandrafalsitta.u2w3d1.entities.Employee;
import kassandrafalsitta.u2w3d1.exceptions.UnauthorizedException;
import kassandrafalsitta.u2w3d1.payloads.EmployeeLoginDTO;
import kassandrafalsitta.u2w3d1.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(EmployeeLoginDTO body) {

        Employee found = this.employeesService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.createToken(found);
        } else {
            throw new UnauthorizedException("Credenziali errate!");
        }


    }
}
