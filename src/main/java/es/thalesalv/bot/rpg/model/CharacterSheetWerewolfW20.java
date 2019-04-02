package es.thalesalv.bot.rpg.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sheet_werewolfw20")
public class CharacterSheetWerewolfW20 {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long characterId;
    private Long playerId;
    private String playerName;
    private String characterName;
}
