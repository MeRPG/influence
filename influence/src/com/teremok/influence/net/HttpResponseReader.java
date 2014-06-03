package com.teremok.influence.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.teremok.influence.util.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Алексей on 03.06.2014
 */
public class HttpResponseReader {

    private static final String RESPONSE = "response";
    private static final String RECORD = "record";

    private static final String PLACE_ATTR = "place";
    private static final String ID_ATTR = "id";
    private static final String INFLUENCE_ATTR = "influence";
    private static final String NAME_ATTR = "name";

    public static int readPlace(Net.HttpResponse httpResponse) {
        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse(httpResponse.getResultAsString());
        XmlReader.Element record = root.getChildByName(RECORD);
        int place = record.getIntAttribute(PLACE_ATTR, -1);

        return place;
    }

    public static List<Record> readRecords(Net.HttpResponse httpResponse) {
        List<Record> records = new LinkedList<>();

        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse(httpResponse.getResultAsString());
        Array<XmlReader.Element> recordElements = root.getChildrenByName(RECORD);
        for (XmlReader.Element recordElement : recordElements) {
            Record record = new Record();
            record.setId(recordElement.getAttribute(ID_ATTR, "--no-id--"));
            record.setName(recordElement.getAttribute(NAME_ATTR, "--no-name--"));
            record.setPlace(recordElement.getIntAttribute(PLACE_ATTR, 0));
            record.setInfluence(recordElement.getIntAttribute(INFLUENCE_ATTR, 0));
            records.add(record);
            Logger.log("read table record: " + record);
        }

        return records;
    }
}
