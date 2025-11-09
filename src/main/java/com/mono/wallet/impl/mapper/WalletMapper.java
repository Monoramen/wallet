package com.mono.wallet.impl.mapper;


import com.mono.wallet.api.dto.WalletResponseDto;
import com.mono.wallet.db.entity.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletResponseDto toResponse(WalletEntity walletEntity);

    @Mapping(target = "balance", ignore = true)
    WalletEntity toEntity(WalletResponseDto walletResponceDto);

}
