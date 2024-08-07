package com.test.casino.mapper;

import com.test.casino.dtos.BetDto;
import com.test.casino.model.entity.Bet;
import com.test.casino.model.entity.GameType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BetMapper {

    @Mapping(target = "gameType", source = "gameType", qualifiedByName = "stringToGameType")
    @Mapping(target = "betAmount", source = "betAmount")
    @Mapping(target = "user.id", source = "userId")
    Bet toBet(BetDto betDto);

    @Mapping(target = "gameType", source = "gameType", qualifiedByName = "gameTypeToString")
    @Mapping(target = "betAmount", source = "betAmount")
    @Mapping(target = "result", source = "result")
    @Mapping(target = "userId", source = "user.id")
    BetDto toBetDto(Bet bet);

    @Mapping(target = "gameType", source = "gameType", qualifiedByName = "gameTypeToString")
    @Mapping(target = "betAmount", source = "betAmount")
    @Mapping(target = "result", source = "result")
    @Mapping(target = "userId", source = "user.id")
    List<BetDto> getBetDtoList(List<Bet> bets);

    @Named("stringToGameType")
    static GameType stringToGameType(String gameType) {
        return GameType.valueOf(gameType);
    }

    @Named("gameTypeToString")
    static String gameTypeToString(GameType gameType) {
        return gameType.name();
    }
}
