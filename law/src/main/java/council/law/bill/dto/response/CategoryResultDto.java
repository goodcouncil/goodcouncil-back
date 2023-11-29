package council.law.bill.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryResultDto {

    private Integer size;
    private String category;
    private List<LawDto> laws;

    public CategoryResultDto(Integer size, String category, List<LawDto> laws) {
        this.size = size;
        this.category = category;
        this.laws = laws;
    }
}
