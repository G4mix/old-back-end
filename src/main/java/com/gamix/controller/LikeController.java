package com.gamix.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.security.JwtManager;
import com.gamix.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    @Operation(tags = "Like", description = "Post likes", security = { @SecurityRequirement(name = "jwt") })
    @GetMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity<Object> likePost(@RequestHeader("Authorization") String token,
            @PathVariable Integer postId,
            @RequestParam(value = "isLiked", defaultValue = "false") boolean isLiked)
            throws ExceptionBase {
        likeService.likePost(JwtManager.getIdFromToken(token), postId, isLiked);
        return ResponseEntity.ok().body(true);
    }

    @Operation(tags = "Like", description = "Comment likes", security = { @SecurityRequirement(name = "jwt") })
    @GetMapping("/comment/{commentId}")
    @ResponseBody
    public ResponseEntity<Object> likeComment(@RequestHeader("Authorization") String token,
            @PathVariable Integer commentId,
            @RequestParam(value = "isLiked", defaultValue = "false") boolean isLiked)
            throws ExceptionBase {
        likeService.likeComment(JwtManager.getIdFromToken(token), commentId, isLiked);
        return ResponseEntity.ok().body(true);
    }

}
