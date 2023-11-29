package council.law.bill.controller;

import council.law.bill.dto.request.CategoryRequestDto;
import council.law.bill.dto.response.ApiResponse;
import council.law.bill.dto.response.CategoryResultDto;
import council.law.bill.dto.response.LawDto;
import council.law.bill.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/law")
public class BillController {
    private final BillService billService;

    @PostMapping("/category")
    public ResponseEntity<ApiResponse> getCategoryLaws(@RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResultDto laws = billService.getCategoryLaws(categoryRequestDto.getCategory());

        return ResponseEntity.ok(
                ApiResponse.builder().status(HttpStatus.OK.value()).message("카테고리 조회 완료").data(laws)
                        .build());
    }

    @GetMapping("/{billId}")
    public ResponseEntity<ApiResponse> getLawDetail(@PathVariable String billId) {
        LawDto law = billService.getLawDetail(billId);

        return ResponseEntity.ok(
                ApiResponse.builder().status(HttpStatus.OK.value()).message("법률안 조회 완료").data(law)
                        .build());
    }
}
