package json.object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Address {
    private Integer id;
    private Integer type;
    private String name;
    private String company;
    private String country;
    private String city;
    private String zipCode;
    private String street;
    private String phone;
    private String email;
    private Integer createdBy;
    private List<Status> statuses;
    private List<Integer> subStatuses;
}
