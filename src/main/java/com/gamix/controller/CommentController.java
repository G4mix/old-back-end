package com.gamix.controller;

import static com.gamix.utils.ControllerUtils.throwError;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.gamix.communication.comment.CommentDTO;
import com.gamix.communication.comment.CommentInput;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.GamixError;
import com.gamix.security.JwtManager;
import com.gamix.models.Comment;
import com.gamix.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import com.gamix.utils.SortUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @Operation(
        tags = "Comment",
        description = "Find all comments of a post",
        security = { @SecurityRequirement(name = "jwt") },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Comments founded with success",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CommentDTO.class))
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Invalid Token supplied or user not exists"
            )
        }
    )
    @GetMapping("/{postId}")
    @ResponseBody
    public ResponseEntity<Object> findAllCommentsOfAPost(
            @RequestHeader("Authorization") @Parameter(hidden=true) String token,
            @PathVariable Integer postId,
            @RequestParam(value = "skip", defaultValue = "0") int skip,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        try {
            Pageable page = PageRequest.of(skip, limit, SortUtils.sortByUpdatedAtOrCreatedAt());
            return ResponseEntity.ok().body(commentService.findAllComments(JwtManager.getUserProfileIdFromToken(token), postId, page));
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(
        tags = "Comment",
        description = "Find all replies of a comment",
        security = { @SecurityRequirement(name = "jwt") },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Comments founded with success",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CommentDTO.class))
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Invalid Token supplied or user not exists"
            )
        }
    )
    @GetMapping("/replies/{commentId}")
    @ResponseBody
    public ResponseEntity<Object> findAllRepliesOfAComment(
            @RequestHeader("Authorization") @Parameter(hidden=true) String token,
            @PathVariable Integer commentId,
            @RequestParam(value = "skip", defaultValue = "0") int skip,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        try {
            Pageable page = PageRequest.of(skip, limit, SortUtils.sortByUpdatedAtOrCreatedAt());
            return ResponseEntity.ok().body(commentService.findAllReplies(JwtManager.getUserProfileIdFromToken(token), commentId, page));
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(
        tags = "Comment",
        description = "Comment in a post",
        security = { @SecurityRequirement(name = "jwt") },
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Comment created in a post with success",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Comment.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Wrong input",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GamixError.class)
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
    @PostMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity<Object> commentPost(
            @RequestHeader("Authorization") @Parameter(hidden=true) String token,
            @PathVariable Integer postId,
            @RequestBody CommentInput commentData
        ) {

        try {
            Comment comment = commentService.commentPost(JwtManager.getIdFromToken(token), postId, commentData.content());
            return ResponseEntity.status(201).body(comment);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(
        tags = "Comment",
        description = "Reply to a comment",
        security = { @SecurityRequirement(name = "jwt") },
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Comment replied with success",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Comment.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Wrong input",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GamixError.class)
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
    @PostMapping("/reply/{commentId}")
    @ResponseBody
    public ResponseEntity<Object> replyComment(
            @RequestHeader("Authorization") @Parameter(hidden=true) String token,
            @PathVariable Integer commentId,
            @RequestBody CommentInput commentData) {
        try {
            Comment comment = commentService.replyComment(JwtManager.getIdFromToken(token), commentId, commentData.content());
            return ResponseEntity.status(201).body(comment);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }
}
