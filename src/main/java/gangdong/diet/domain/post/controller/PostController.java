package gangdong.diet.domain.post.controller;

import gangdong.diet.domain.post.dto.PostRequest;
import gangdong.diet.domain.post.dto.PostResponse;
import gangdong.diet.domain.post.dto.PostSearchResponse;
import gangdong.diet.domain.post.service.PostService;
import gangdong.diet.global.auth.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "게시물 API")
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
@RestController
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시물 목록 조회", description = "키워드를 통해 게시물을 검색합니다. 키워드 간의 구분은 ,과 같은 쉼표로 합니다.")
    @GetMapping("") // 뭐라고 이름 줄까?
    public ResponseEntity<Slice<PostSearchResponse>> getPostsByKeywords(@RequestParam(value = "category") String category,
                                                                        @RequestParam(value = "cursorId", required = false) Long cursorId,
                                                                        @RequestParam(value = "keywords", required = false) String keywords,
                                                                        @RequestParam(value = "size") int size) {
        return ResponseEntity.ok().body(postService.findByKeywords(category, cursorId, keywords, size));
    }

    @Operation(summary = "게시물 상세 조회", description = "게시물 id를 통해 게시물 1개를 조회합니다.")
    @GetMapping("/{recipeId}")
    public ResponseEntity<PostResponse> getOnePostById(@PathVariable Long recipeId) {
        return ResponseEntity.ok().body(postService.getOnePost(recipeId));
    }

    @Operation(summary = "게시물 저장")
    @PostMapping
    public ResponseEntity<String> createPost(@RequestPart("postRequest") @Validated PostRequest postRequest,
                                             @AuthenticationPrincipal MemberDetails memberDetails) {
        postService.savePost(postRequest, memberDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body("게시물 저장을 완료했습니다.");
    }


    @PutMapping("/{recipeId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long recipeId,
                                                   @RequestPart("postRequest") @Validated PostRequest postRequest,
//                                                   @RequestPart("thumbnail") MultipartFile thumbnail,
//                                                   @RequestPart(value = "postImages", required = false) List<MultipartFile> postImages,
                                                   @AuthenticationPrincipal MemberDetails memberDetails) {

        return ResponseEntity.ok().body(postService.updatePost(recipeId, postRequest, memberDetails));
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deletePost(@PathVariable Long recipeId, @AuthenticationPrincipal MemberDetails memberDetails) {
        postService.deletePost(recipeId, memberDetails);
        return ResponseEntity.ok().body("삭제가 완료됐습니다.");
    }

}
