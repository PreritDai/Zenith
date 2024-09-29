package Common;

import LexicalAnalyser.Constants.DataTypes;

import java.util.ArrayList;

public class SymbolTable {
    private int SN = 0;
    private ArrayList<Row> rows = new ArrayList<>();


    public class Row {
        public int SN;
        public String identifier;
        public DataTypes dataType;

        Row(int SN, String identifier, DataTypes dataType) {
            this.SN = SN;
            this.identifier = identifier;
            this.dataType = dataType;
        }
    }

    public void addData(String identifier, DataTypes dataType) {
        Row row = new Row(SN, identifier, dataType);
        SN++;
        rows.add(row);
    }

    public ArrayList<Row> getTable() {
        return rows;
    }

    public Row getRow(int SN) {
        return rows.get(SN);
    }
}
