//package br.com.aprendeai.controller;
//
//import java.util.List;
//
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import br.com.aprendeai.dtos.PostRequestDTO;
//import br.com.aprendeai.model.Post;
//import br.com.aprendeai.service.PostService;
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/posts")
//@RequiredArgsConstructor
//public class PostController {
//    private final PostService postService;
//    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })    
//    public ResponseEntity<Post> criarPost(@RequestPart("dados") PostRequestDTO postRequest,            
//    		@RequestPart(value = "arquivos", required = false) List<MultipartFile> arquivos) {
//        Post post = postService.criarPost(postRequest, arquivos);        
//        return ResponseEntity.ok(post);    
//    }
//}        