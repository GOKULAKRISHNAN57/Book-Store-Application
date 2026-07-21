package BridgeLabz.Book_Store_Application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtUtil jwtUtil;

    @Override
    public String generateToken(UserDetails userDetails) {
        return jwtUtil.generateToken(userDetails);
    }

    @Override
    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtUtil.validateToken(token, userDetails);
    }
}