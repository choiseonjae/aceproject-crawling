package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.ExcelRow;

public class BaseBallPlayerRecordProvider {

	private static final String DEFAULT_URL = "https://www.koreabaseball.com";
	private static final String SEARCH_PLAYER_URL = "/ws/Controls.asmx/GetSearchPlayer";

	public static void seachAndMakeExcel(String name) {

		// 검색할 이름
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);

		// JSON 형태로 받은 선수의 정보(태그 제거 상태)
		Document doc = Crawling.post(DEFAULT_URL + SEARCH_PLAYER_URL, params);
		String json = doc.text();
		System.out.println(json);

		JsonParser parser = new JsonParser();

		// 선수 정보들만 추출
		JsonObject object = (JsonObject) parser.parse(json);

		// 엑셀에 저장할 내용
		List<ExcelRow> excel = new ArrayList<ExcelRow>();
		
		addPlayerInExcel(excel, object, "now");
		
		addPlayerInExcel(excel, object, "retire");
		
		Excel.write("/Users/ace-004/Desktop/" + name + ".xls", excel);
	}

	private static void addPlayerInExcel(List<ExcelRow> excel, JsonObject object, String type) {

		// 선수 한명, 한명을 분리
		JsonArray retirePlayers = (JsonArray) object.get(type);

		excel.add(new ExcelRow(type));
		excel.add(new ExcelRow());

		for (int i = 0; i < retirePlayers.size(); i++) {

			// 배열중 하나의 player 정보만 가져옴
			JsonObject player = (JsonObject) retirePlayers.get(i);

			// 주소가 ""url""안에 있는 형태여서 제거 해줌
			String pLink = player.get("P_LINK").getAsString();

			// 최종 주소
			String url = DEFAULT_URL + pLink;

			// 알고자 하는 내용이 담긴 tag
			String tagFilter = "div.sub-content";
			String playerInfoTagFilter = "div.player_info ul.list02 li";
			String playerRecordTagFilter = "div.player_records table";

			// 추출
			Element info = Crawling.get(url).selectFirst(tagFilter);
			Elements playerInfoTable = info.select(playerInfoTagFilter);
			playerInfoTable.forEach(p -> {
				List<String> infoRecord = new ArrayList<String>();
				infoRecord.add(p.select("strong").first().text());
				infoRecord.add(p.select("span").first().text());
				excel.add(new ExcelRow(infoRecord));

			});
			Element playerRecordTable = info.selectFirst(playerRecordTagFilter);

			// 스키마 저장
			List<String> schema = playerRecordTable.select("thead tr th").eachText();
			excel.add(new ExcelRow(schema));

			// 선수의 전체 기록
			Iterator<Element> playerRecords = playerRecordTable.select("tbody tr").iterator();

			// 하나의 기록
			while (playerRecords.hasNext()) {
				List<String> data = playerRecords.next().select("td").eachText();
				excel.add(new ExcelRow(data));
			}

			// 마지막 통산 기록
			List<String> playerTotalRecords = playerRecordTable.select("tfoot th").eachText();
			playerTotalRecords.add(0, "");
			excel.add(new ExcelRow(playerTotalRecords));

			// 한줄 띄우기
			excel.add(new ExcelRow());
		}
	}

}