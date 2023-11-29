package council.law.bill.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import council.law.bill.dto.response.CategoryResultDto;
import council.law.bill.dto.response.LawDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillService {
    private String stringUrl = "https://open.assembly.go.kr/portal/openapi/nxjuyqnxadtotdrbw";
    private String type = "json";

    @Value("${key}")
    private String key;

    public CategoryResultDto getCategoryLaws(String category) {

        Integer pIndex = 1;
        Integer pSize = 1000;

        String finalUrl = stringUrl + "?KEY=" + key + "&Type=" + type + "&pIndex=" + pIndex + "&pSize=" + pSize + "&AGE=" + "21";

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String selectedCommittee = null;
        switch (category) {
            case "고용":
                selectedCommittee = "산업통상자원중소벤처기업위원회";
                break;
            case "보건":
                selectedCommittee = "보건복지위원회";
                break;
            case "기술":
                selectedCommittee = "과학기술정보방송통신위원회";
                break;
            case "환경":
                selectedCommittee = "환경노동위원회";
                break;
            case "여성":
                selectedCommittee = "여성가족위원회";
                break;
            case "법":
                selectedCommittee = "법제사법위원회";
                break;
            case "금융":
                selectedCommittee = "기획재정위원회";
                break;
            case "예술":
                selectedCommittee = "문화체육관광위원회";
                break;
            case "식품":
                selectedCommittee = "농림축산식품해양수산위원회";
                break;
            case "교육":
                selectedCommittee = "교육위원회";
                break;
            case "국방":
                selectedCommittee = "국방위원회";
                break;
            case "외교":
                selectedCommittee = "외교통일위원회";
                break;
            case "교통":
                selectedCommittee = "국토교통위원회";
                break;
            case "안전":
                selectedCommittee = "행정안전위원회";
                break;
        }

        List<LawDto> lawList = new ArrayList<>();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(finalUrl, String.class);

            JsonNode root = objectMapper.readTree(responseEntity.getBody());
            JsonNode lawsNode = root.path("nxjuyqnxadtotdrbw").get(1).get("row");

            for (JsonNode node : lawsNode) {
                String age = node.hasNonNull("AGE") ? node.get("AGE").asText() : null;
                String billId = node.hasNonNull("BILL_ID") ? node.get("BILL_ID").asText() : null;
                String billNo = node.hasNonNull("BILL_NO") ? node.get("BILL_NO").asText() : null;
                String billName = node.hasNonNull("BILL_NAME") ? node.get("BILL_NAME").asText() : null;
                String proposer = node.hasNonNull("PROPOSER") ? node.get("PROPOSER").asText() : null;
                String proposerKind = node.hasNonNull("PROPOSER_KIND") ? node.get("PROPOSER_KIND").asText() : null;
                String proposeDt = node.hasNonNull("PROPOSE_DT") ? node.get("PROPOSE_DT").asText() : null;
                String procResultCd = node.hasNonNull("PROC_RESULT_CD") ? node.get("PROC_RESULT_CD").asText() : null;
                String committeeId = node.hasNonNull("CURR_COMMITTEE_ID") ? node.get("CURR_COMMITTEE_ID").asText() : null;
                String committee = node.hasNonNull("CURR_COMMITTEE") ? node.get("CURR_COMMITTEE").asText() : null;
                String procDt = node.hasNonNull("PROC_DT") ? node.get("PROC_DT").asText() : null;
                String linkUrl = node.hasNonNull("LINK_URL") ? node.get("LINK_URL").asText() : null;

                if (category.equals("전체")) {
                    LawDto lawDto = new LawDto(age, billId, billNo, billName, proposer, proposerKind, proposeDt, procResultCd, committeeId, committee, procDt, linkUrl);
                    lawList.add(lawDto);
                } else {
                    if (committee.equals(selectedCommittee)) {
                        LawDto lawDto = new LawDto(age, billId, billNo, billName, proposer, proposerKind, proposeDt, procResultCd, committeeId, committee, procDt, linkUrl);
                        lawList.add(lawDto);
                    }
                }

                // 법률안 100개 되면 종료
                if(lawList.size() == 100){
                    break;
                }
            }

        } catch (RestClientException e) {
            System.err.println("An error occurred while sending the request: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("An error occurred while parsing the response: " + e.getMessage());
        }


        CategoryResultDto categoryResultDto = new CategoryResultDto(lawList.size(), category, lawList);
        return categoryResultDto;
    }

    public LawDto getLawDetail(String selectedBillId) {
        String finalUrl = stringUrl + "?KEY=" + key + "&Type=" + type + "&pIndex=" + "&AGE=" + "21" + "&bill_id=" + selectedBillId;

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        LawDto lawDto = new LawDto();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(finalUrl, String.class);

            JsonNode root = objectMapper.readTree(responseEntity.getBody());
            JsonNode node = root.path("nxjuyqnxadtotdrbw").get(1).get("row").get(0);

            String age = node.hasNonNull("AGE") ? node.get("AGE").asText() : null;
            String billId = node.hasNonNull("BILL_ID") ? node.get("BILL_ID").asText() : null;
            String billNo = node.hasNonNull("BILL_NO") ? node.get("BILL_NO").asText() : null;
            String billName = node.hasNonNull("BILL_NAME") ? node.get("BILL_NAME").asText() : null;
            String proposer = node.hasNonNull("PROPOSER") ? node.get("PROPOSER").asText() : null;
            String proposerKind = node.hasNonNull("PROPOSER_KIND") ? node.get("PROPOSER_KIND").asText() : null;
            String proposeDt = node.hasNonNull("PROPOSE_DT") ? node.get("PROPOSE_DT").asText() : null;
            String procResultCd = node.hasNonNull("PROC_RESULT_CD") ? node.get("PROC_RESULT_CD").asText() : null;
            String committeeId = node.hasNonNull("CURR_COMMITTEE_ID") ? node.get("CURR_COMMITTEE_ID").asText() : null;
            String committee = node.hasNonNull("CURR_COMMITTEE") ? node.get("CURR_COMMITTEE").asText() : null;
            String procDt = node.hasNonNull("PROC_DT") ? node.get("PROC_DT").asText() : null;
            String linkUrl = node.hasNonNull("LINK_URL") ? node.get("LINK_URL").asText() : null;

            lawDto = LawDto.builder().age(age).billId(billId).billNo(billNo).billName(billName).proposer(proposer).proposerKind(proposerKind).proposeDt(proposeDt).procResultCd(procResultCd).committeeId(committeeId).committee(committee).procDt(procDt).linkUrl(linkUrl)
                    .build();

        } catch (RestClientException e) {
            System.err.println("An error occurred while sending the request: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("An error occurred while parsing the response: " + e.getMessage());
        }

        return lawDto;
    }
}
