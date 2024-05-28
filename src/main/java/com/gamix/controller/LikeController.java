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
import com.gamix.exceptions.GamixError;
import com.gamix.security.JwtManager;
import com.gamix.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    @Operation(
        tags = "Like",
        description = "Post likes",
        security = { @SecurityRequirement(name = "jwt") },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Like post executed with success",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "true")
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Invalid Token supplied or user not exists"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Resource not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GamixError.class)
                )
            )
        }
    )
    @GetMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity<Object> likePost(@RequestHeader("Authorization") String token,
            @PathVariable Integer postId,
            @RequestParam(value = "isLiked") boolean isLiked)
            throws ExceptionBase {
        likeService.likePost(JwtManager.getIdFromToken(token), postId, isLiked);
        return ResponseEntity.ok().body(true);
    }

    @Operation(
        tags = "Like",
        description = "Comment likes",
        security = { @SecurityRequirement(name = "jwt") },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Like comment executed with success",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "true")
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Invalid Token supplied or user not exists"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Resource not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GamixError.class)
                )
            )
        }
    )
    @GetMapping("/comment/{commentId}")
    @ResponseBody
    public ResponseEntity<Object> likeComment(@RequestHeader("Authorization") String token,
            @PathVariable Integer commentId,
            @RequestParam(value = "isLiked") boolean isLiked)
            throws ExceptionBase {
        likeService.likeComment(JwtManager.getIdFromToken(token), commentId, isLiked);
        return ResponseEntity.ok().body(true);
    }

}
