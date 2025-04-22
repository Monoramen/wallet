package com.mono.wallet.impl.mapper;

import com.mono.wallet.api.dto.WalletRequestDTO;
import com.mono.wallet.api.dto.WalletResponseDTO;
import org.mapstruct.Mapper;
import com.mono.wallet.db.entity.WalletEntity;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletResponseDTO toResponse(WalletEntity entity);
    @Mapping(target = "balance", ignore = true)
    WalletEntity toEntity(WalletRequestDTO dto);
}
