package org.example.ezyshop.dto.address;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.dto.user.UserDTO;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AddressResponse extends BaseResponse {
    private List<AddressDTO> addressDTOS;
    private AddressDTO addressDTO;

    public AddressResponse(List<AddressDTO> addressDTOS) {
        this.addressDTOS = addressDTOS;
    }

    public AddressResponse(boolean success, int code) {
        super(success, code);
    }

    public AddressResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}
