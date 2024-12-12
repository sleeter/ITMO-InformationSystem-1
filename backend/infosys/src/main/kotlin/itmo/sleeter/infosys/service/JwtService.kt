package itmo.sleeter.infosys.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import itmo.sleeter.infosys.configuration.jwt.JwtProperties
import itmo.sleeter.infosys.model.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Date


@Service
class JwtService(val props: JwtProperties) {
    fun extractUserName(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }
    fun generateToken(userDetails: UserDetails): String {
        val claims: MutableMap<String, Any> = HashMap()
        if (userDetails is User) {
            claims["id"] = userDetails.id!!
            claims["role"] = userDetails.role!!
        }
        return generateToken(claims, userDetails)
    }
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val userName = extractUserName(token)
        return (userName == userDetails.username) && !isTokenExpired(token)
    }
    private fun <T> extractClaim(token: String, claimsResolvers: (Claims) -> T): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolvers(claims)
    }
    private fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * props.exp))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }
    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }
    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }
    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJwt(token).body
    }
    private fun getSigningKey(): Key {
        val keyBytes: ByteArray = Decoders.BASE64.decode(props.secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}