package subway.dto;

import subway.Entity.SectionEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.response.LineResponse;
import subway.dto.response.StationResponse;

public class DtoMapper {

    public static SectionEntity convertToSectionEntity(Section section) {
        return new SectionEntity(
                section.getId(),
                section.getUpward().getId(),
                section.getDownward().getId(),
                section.getDistance(),
                section.getLine().getId()
        );
    }

    public static StationResponse convertToStationReponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static LineResponse convertToLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }
}
