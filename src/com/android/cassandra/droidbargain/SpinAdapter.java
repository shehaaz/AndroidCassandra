public class SpinAdapter extends ArrayAdapter<StoreFactory>{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private User[] values;

    public SpinAdapter(Context context, int textViewResourceId,
            ArrayList<StoreFactory> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
       return values.length;
    }

    public User getItem(int position){
       return values.get(position);
    }

    public long getItemId(int position){
       return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);

        label.setText(values.get(position).getStoreTitle());


        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getStoreTitle());

        return label;
    }
}
