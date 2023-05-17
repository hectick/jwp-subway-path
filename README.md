# jwp-subway-path

### 더미 데이터

```sql
INSERT INTO line(name, color, extra_fare) VALUES ('1호선', '남색', 400);

INSERT INTO station(name)
VALUES ('강변');
INSERT INTO station(name)
VALUES ('잠실나루');
INSERT INTO station(name)
VALUES ('잠실');
INSERT INTO station(name)
VALUES ('잠실새내');
INSERT INTO station(name)
VALUES ('종합운동장');

INSERT INTO station(name)
VALUES ('노량진');
INSERT INTO station(name)
VALUES ('용산');
INSERT INTO station(name)
VALUES ('서울역');

INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (null, 6, null, 1);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (6,7, 12, 1);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (7,8, 20, 1);
INSERT INTO section(upward_id, downward_id, distance, line_id) VALUES (8, null, null, 1);
```

### 요청과 응답

#### Request POST /lines

```json
{
  "name": "2호선",
  "color": "초록색",
  "extraFare": 0
}
```

#### Request POST /lines/{lineId}/stations

```json
{
  "upwardStationId": 1,
  "downwardStationId": 3,
  "distance": 10
}
```

#### Response GET /lines/{lineId}

```json
{
  "id": 2,
  "name": "2호선",
  "color": "초록색",
  "extraFare": 0,
  "stations": [
    {
      "id": 1,
      "name": "강변"
    },
    {
      "id": 3,
      "name": "잠실"
    }
  ]
}
```

### 기능 목록

1. API 기능 요구사항 명세

- [x] 노선에 역 등록 API 신규 구현
    - [x] 노선에 구간을 등록한다.
        - [x] POST `/lines/{lineId}/stations`
        - [x] 노선 id, 구간 거리, 구간의 상행 방향 역 id, 구간의 하행 방향 역 id
- [x] 노선에 역 제거 API 신규 구현
    - [x] 해당 노선에서 역을 제거한다.
        - [x] DELETE `/lines/{lineId}/stations/{stationId}`
        - [x] 노선 id, 삭제할 역의 id
- [x] 노선 조회 API 기능 구현
    - [x] GET `/lines/{lineId}`
    - [x] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선한다.
        - [x] 역을 추가할 때는 이전역 다음역 정보를 같이 저장함으로써 순서를 표시한다.
        - [x] 이전역, 다음역 정보를 이용해서 도메인을 순서에 맞게 조립한다.
- [x] 노선 목록 조회 API 기능 구현
    - [x] GET `/lines`
    - [x] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선한다.
- [x] 경로 조회 API 기능 구현
    - [x] GET `/paths?from={stationId}&to={stationId}&age={age}`
        - age는 adult, teenager, child가 있다.
    - [x] 출발역과 도착역 사이의 최단 거리 경로를 구한다.
    - [x] 최단 거리 경로와 함께 총 거리 정보를 함께 응답한다.
    - [x] 한 노선에서 경로 찾기 뿐만 아니라 여러 노선의 환승도 고려한다.
    - [x] 경로 조회 시 요금 정보를 포함하여 응답합니다.

2. 도메인 기능 명세

노선은 기본적으로 `null - 역1 - 역 2 - 역3 - null` 형태로 구성된다.
편의상 왼쪽 끝의 null을 상행 종점, 오른쪽 끝의 null을 하행 종점이라고 정리한다.

- 노선(Line)
    - [x] 노선은 id, 이름, 해당 노선에 있는 구간들의 정보를 갖는다.
    - [x] 노선이 처음 생성되면 `상행 종점(null) - 하행 종점(null)` 구간만을 갖는다.
    - [x] 새로운 구간을 생성한다.
        - [x] 노선에 역이 추가될 때는 `상행 방향 역 - 하행 방향 역`구간이 `상행 방향 역 - 새로운 역`과 `새로운 역 - 하행 방향 역`으로 나뉜다.
            - [x] 구간 사이에 역이 추가될 때, `구간 내 두 역과 새로운 역 사이 거리 각각의 합 != 구간의 기존 거리` 인 경우 예외처리한다.
                - 단, 상행 종점이나 하행 종점이 껴있을 경우 구간 거리가 null 이므로 제외한다.
    - [x] 기존 구간을 삭제한다.
        - [x] 노선에서 역이 제거될 때는 `상행 방향 역 - 제거할 역`과 `제거할 역 - 하행 방향 역` 두 구간이 `상행 방향 역 - 하행 방향 역`으로 합쳐진다.
            - [x] 제거되는 역과 상행 방향 역, 하행 방향 역 사이의 거리의 합이 합쳐진 구간에 저장되어야 한다.
        - [x] 노선에 남은 마지막 역 두개를 제거할 때는 `null - 상행 종점`, `상행 종점 - 하행 종점`, `하행 종점 - null` 세 구간이 삭제된다.
            - [x] 제거 처리 이후 구간이 두개만 남은 경우, 모든 구간을 삭제한다.
    - [x] 노선을 구성하는 역들을 `상행 종점 -> 하행 종점` 방향으로 정렬해서 반환한다.

- 역(Station)
    - [x] 역은 id, 이름을 갖는다.
    - [x] 상행 종점과 하행 종점을 나타내기 위한 빈 역은 역 이름을 null로 갖는다.
    - [x] 역 이름이 동일한지 확인할 수 있다.

- 구간(Section)
    - [x] 구간은 id, 상행 방향 역(Station) 정보, 하행 방향 역(Station) 정보, 역 사이 거리, 노선을 갖는다.
    - [x] 역 간의 연결정보는 section을 생성하며 만들어진다.
    - [x] 상행 방향 역과 하행 방향 역이 동일하면 예외처리한다.

- 거리(Distance)
    - [x] 거리 정보가 양수가 아닐 경우 예외처리한다.
    - [x] 구간에 상행 종점 또는 상행 종점이 포함될 경우 거리는 null 로 저장된다.

- 지하철가이드(SubwayMap)
    - [x] 출발역과 도착역이 주어지면 최단 경로의 구간들과 요금을 알려준다.

- 경로(Path)
    - [x] 출발역과 도착역이 주어지면 최단 경로를 구한다.
    - [x] 최단 경로의 거리를 계산한다.

- 운임요금(Fare)
    - [x] 거리가 주어지면 거리에 해당하는 요금을 계산한다.
    - [x] 기본운임(10㎞ 이내): 기본운임 1,250원
    - [x] 이용 거리 초과 시 추가운임 부과한다.
        - [x] 10km~50km: 5km 까지 마다 100원 추가
        - [x] 50km 초과: 8km 까지 마다 100원 추가
    - [x] 추가 요금 정책을 반영한다.
        - [x] 노선별 추가 요금 정책을 반영한다.
            - 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가한다.
                - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
                - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
            - 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다.
                - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
        - [x] 연령별 요금 할인 정책을 반영한다.
            - 청소년: 운임에서 350원을 공제한 금액에서 20%할인
            - 어린이: 운임에서 350원을 공제한 금액에서 50%할인

### 숙제 - 예외 상황 검증

- [x] 잠실을 추가하고 싶은 노선에 잠실이 이미 있는지 확인한다.
- [x] 노선의 역이 2개 초과로 남은 상황인데 2개의 역을 동시에 삭제 요청하는 경우 예외처리
- [x] 노선에 마지막 두개의 역만 남았을 때 하나의 역을 제거한다는 요청이 들어오면 다 삭제
