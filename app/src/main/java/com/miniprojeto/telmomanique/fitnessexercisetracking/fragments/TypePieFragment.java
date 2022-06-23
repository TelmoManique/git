package com.miniprojeto.telmomanique.fitnessexercisetracking.fragments;

public class TypePieFragment {
}
/*private void drawTypeChar() {
        int total = 0;
        Map<String , Integer> typeMap = new HashMap<>();
        for(Routine r : routines){
            for (Map.Entry<String, Integer> entry : r.getTypeMap().entrySet()) {
                total += entry.getValue();
                if( typeMap.containsKey(entry.getKey()) )
                    typeMap.put( entry.getKey() , typeMap.get(entry.getKey()) + entry.getValue());
                else
                    typeMap.put( entry.getKey() ,  entry.getValue());
            }
        }

        PieChart musclePie = getActivity().findViewById(R.id.pieChartMuscleGroup);

        List<PieEntry> entries = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Integer> entry : muscleMap.entrySet()){
            entries.add( new PieEntry( entry.getValue() , entry.getKey()));
        }

        PieDataSet set = new PieDataSet(entries, "Most Exercised Muscle Group");
        PieData data = new PieData(set);

        Legend l= musclePie.getLegend();
        l.setEnabled(false);

        set.setSliceSpace(8);
        set.setUsingSliceColorAsValueLineColor(true);
        set.getValueLineColor();
        musclePie.setUsePercentValues(true);

        //TODO STYLE
        data.setValueTextColor(Color.BLACK);
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setValueLinePart1OffsetPercentage(10.f);
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(20);
        musclePie.setEntryLabelColor(Color.BLACK);


        musclePie.setData(data);
        musclePie.invalidate(); // refresh
    }
 */
