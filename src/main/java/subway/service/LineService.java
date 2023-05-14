package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Lines;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(final LineRequest request) {
        Lines lines = Lines.from(lineRepository.findAll());
        Line newLine = Line.of(request.getName(), request.getColor());
        lines.add(newLine);
        Line insertedLine = lineRepository.insert(newLine);
        return LineResponse.from(insertedLine);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse findLineById(final long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        return LineResponse.from(line);
    }

    //todo : line name, color 검증 후 DB 삽입하도록 수정
    public void updateLine(final long id, final LineRequest request) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        lineRepository.update(Line.of(id, request.getName(), request.getColor(), line.getSections()));
    }

    public void deleteLineById(final long id) {
        lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        lineRepository.deleteById(id);
    }

}
