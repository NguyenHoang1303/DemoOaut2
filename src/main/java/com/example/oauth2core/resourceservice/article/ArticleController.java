package com.example.oauth2core.resourceservice.article;

import com.example.oauth2core.entity.User;
import com.example.oauth2core.responseapi.ResponseApi;
import com.example.oauth2core.util.JwtHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RestController
@RequestMapping("api/v1/articles")
@Log4j2
public class ArticleController {

    @Autowired
    ArticleRepository repo;

    @Autowired
    JwtHandler jwtHandler;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll(HttpServletRequest request, HttpServletResponse response){
        try {
            User user = jwtHandler.decodeToken(request,response);
            ResponseApi res = new ResponseApi();
            if (user == null){
                res.setMessage("Không có quyền");
                res.setHttpCode(403);
                return new ResponseEntity<>( res, HttpStatus.FORBIDDEN);
            }
            String scope = user.getScope();
            if (!scope.equals("api/v1/articles.read")){
                return new ResponseEntity<>( new HashMap<>().put("error", "không có quyền"), HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new HashMap<>().put("error", e.getMessage()), HttpStatus.FORBIDDEN) ;
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "init-data")
    public String saveAll(){
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("Chelsea lần đầu đá tại Bernabeu","Đội bóng thành London sẽ lần đầu thi đấu trên thảm cỏ Bernabeu, khi gặp Real ở tứ kết lượt về Champions League hôm nay 12/4."));
        articles.add(new Article("Ramos: Tôi có thể chơi đỉnh cao 5 năm nữa","Trung vệ 36 tuổi Sergio Ramos lạc quan sẽ sớm lấy lại phong độ cao nhất sau gần một mùa vật lộn với chấn thương ở PSG."));
        articles.add(new Article("Nagelsmann: Villarreal sai lầm vì để Bayern sống sót","HLV Julian Nagelsmann nói rằng Villarreal sẽ bị trừng phạt ở lượt về tứ kết Champions League vì không thắng đậm Bayern từ lượt đi."));
        articles.add(new Article("Djokovic bị chỉ trích tự làm bẩn sự nghiệp","Cựu số một thế giới Marcelo Rios cho rằng Novak Djokovic không tiêm vaccine phòng Covid-19 vì kiêu ngạo, khiến sự nghiệp bị vấy bẩn"));
        articles.add(new Article("Djokovic có thể gặp Alcaraz ở tứ kết Monte Carlo Masters","Novak Djokovic và Carlos Alcaraz có cơ hội gặp nhau lần đầu tiên nếu cùng vào tứ kết sự kiện Masters 1000 tuần tới tại Monaco"));
        articles.add(new Article("Van Basten: Thà xem Netflix còn hơn xem Atletico","Huyền thoại bóng đá Hà Lan Marco van Basten chỉ trích chiến thuật biển người của HLV Diego Simeone."));
        try {
            repo.saveAll(articles);
            return "success";
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
