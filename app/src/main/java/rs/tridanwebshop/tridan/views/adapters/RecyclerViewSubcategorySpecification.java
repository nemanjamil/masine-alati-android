package rs.tridanwebshop.tridan.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.SubCategoryArticlesActivity;
import rs.tridanwebshop.tridan.common.utils.Log;
import rs.tridanwebshop.tridan.customview.MultiSelectionSpinner;
import rs.tridanwebshop.tridan.models.categories.category_specification.Detail;
import rs.tridanwebshop.tridan.models.categories.category_specification.Spec;

public class RecyclerViewSubcategorySpecification extends RecyclerView.Adapter<RecyclerViewSubcategorySpecification.MyViewHolder> {

    private List<Spec> specification;
    private Context context;
    private MultiSelectionSpinner multiSelectionSpinner;

    private TextView specificationGroup;

    public class MyViewHolder extends RecyclerView.ViewHolder implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {

        public MyViewHolder(View view) {

            super(view);
            specificationGroup = (TextView) view.findViewById(R.id.specificationGroup);
            multiSelectionSpinner = (MultiSelectionSpinner) view.findViewById(R.id.multiSelectionSpinner);
            multiSelectionSpinner.setListener(this);
        }

        @Override
        public void selectedIndices(List<Integer> indices) {

        }

        @Override
        public void selectedStrings(List<String> strings) {

            boolean found = false;
            List<Integer> ids = new ArrayList<>();

            ((SubCategoryArticlesActivity) context).clearSelectedSpecification();
            Log.logInfo("MULTISPINNER", strings.toString());
            for (Spec spec : specification) {
                for (Detail detail : specification.get(specification.indexOf(spec)).getDetalj()) {
                    if (strings.contains(detail.getIdSpecVrednostiImeVre())) {

                        Log.logInfo("MULTISPINNER", "" + detail.getIdSpecVrednostiVre());
                        ids.add(detail.getIdSpecVrednostiVre());
                        found = true;
                    }
                    if (ids.size() > 0) {
                        found = true;
                        ((SubCategoryArticlesActivity) context).setSelectedSpecifications(spec.getIdSpecGrupe(), ids);
                    }
                }
                if (found) break;
            }

        }
    }


    public RecyclerViewSubcategorySpecification(Context context, List<Spec> specification) {

        this.specification = specification;
        this.context = context;
        Log.logInfo("SORT NUMBER SELECTED", "" + ((SubCategoryArticlesActivity) context).getSelectedSpecification().size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.specification_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewSubcategorySpecification.MyViewHolder holder, int position) {

        List<String> myCollection = new ArrayList<>();
        List<Integer> selectedSpecification = new ArrayList<>();
        for (int i = 0; i < specification.get(position).getDetalj().size(); i++) {
            myCollection.add(specification.get(position).getDetalj().get(i).getIdSpecVrednostiImeVre());
        }
        specificationGroup.setText(specification.get(position).getGrupe());
        multiSelectionSpinner.setItems(myCollection);
        if (((SubCategoryArticlesActivity) context).getSelectedSpecification().containsKey(specification.get(position).getIdSpecGrupe())) {
            for (int i = 0; i < specification.get(position).getDetalj().size(); i++) {
               if (((SubCategoryArticlesActivity) context).getSelectedSpecification().get(specification.get(position).getIdSpecGrupe()).contains(specification.get(position).getDetalj().get(i).getIdSpecVrednostiVre()))
                selectedSpecification.add(i);
            }
        }
        multiSelectionSpinner.setSelections(selectedSpecification);
    }

    @Override
    public int getItemCount() {
        return specification.size();
    }

}
