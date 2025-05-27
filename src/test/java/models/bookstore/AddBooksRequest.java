package models.bookstore;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AddBooksRequest {
    private String userId;
    private List<IsbnItem> collectionOfIsbns;

    @Data
    @AllArgsConstructor
    public static class IsbnItem {
        private String isbn;
    }
}