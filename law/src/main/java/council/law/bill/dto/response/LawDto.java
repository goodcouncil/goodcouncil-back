package council.law.bill.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LawDto {

    private String age;
    private String billId;
    private String billNo;
    private String billName;
    private String proposer;
    private String proposerKind;
    private String proposeDt;
    private String procResultCd;
    private String committeeId;
    private String committee;
    private String procDt;
    private String linkUrl;

}
