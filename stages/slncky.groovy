/***********************************************************
 ** Stages run lnc RNA analysis with slncky with python 3
 ** Author: Nikhil Shinde <sd1172@srmist.edu.in>
 ** Last Update: 30/09/2024
 *********************************************************/

//Python 2.7 and slncky
slncky_dir="slncky_out"

prepare_annotations_bed = {
	output.dir=slncky_dir
	if (liftover = "" && noncoding = ""){
	produce("Ref_genome.protein_coding.bed","Rel_ref_genome.protein_coding.bed"){
	exec """
	$python3 $ensembl_gtf2bed $annotation Ref_genome.prefix.prefix ;
 	$python3 $annotation_related_species $annotation Ref_genome.prefix.prefix
	"""
	}
	} else {
	produce("Ref_genome.protein_coding.bed","Rel_ref_genome.protein_coding.bed"){
	exec """
	$gffread $annotation --bed -o ${output.dir}/temp.bed ;
	cut -f1-12 ${output.dir}/temp.bed > $output1 && rm ${output.dir}/temp.bed ;
	$gffread $annotation_related_species --bed -o ${output.dir}/temp1.bed ;
        cut -f1-12 ${output.dir}/temp1.bed > $output2 && rm ${output.dir}/temp1.bed ;
	"""
	}
	}
}

fasta_index = {
    if ('${genome}.fai' != "" && '${genome_related_species}.fai' != ""){
        exec """
        $samtools faidx ${genome} ;
        $samtools faidx ${genome_related_species}
        """
    } else {
        exec "echo \"Required .fai files are already there for genome or related species.\""
    }
}

prepare_Liftover = {
	output.dir = slncky_dir
	if (liftover = ""){
	produce(org_name+"to"+rel_sp_name+".over.chain.gz"){
	exec """
 	$python3 $Liftover $threads $genome $org_name $genome_related_species $rel_sp_name $distance ;
  	mv ${output.dir}/${org_name}.${org_name}.over.chain.gz $output
   	"""
	} else {
	exec "echo 'Liftover files are provided for conservation studies'"
	}
	}
}
  	
annotation_config = {
    output.dir = slncky_dir
	if (liftover != "" && noncoding != ""){ 
	from("Ref_genome.protein_coding.bed","Rel_ref_genome.protein_coding.bed") produce("annotation.config") {
            exec """
            echo '>'$org_name >> $output ;
            echo 'CODING='$input1 >> $output ;
            echo 'GENOME_FA='$genome >> $output ;
            echo 'ORTHOLOG='$rel_sp_name >> $output ;
            echo 'LIFTOVER='$liftover >> $output ;
            echo 'NONCODING='$noncoding >> $output ;
            echo 'MIRNA='$mir >> $output ;
            echo 'SNORNA='$sno >> $output ;
            echo '>'$rel_sp_name >> $output ;
            echo 'CODING='$input2 >> $output ;
            echo 'GENOME_FA='$genome_related_species >> $output ;
            echo 'ORTHOLOG='$org_name >> $output ;
            echo 'NONCODING='$rel_noncoding >> $output ;
            echo 'MIRNA='$rel_mir >> $output ;
            echo 'SNORNA='$rel_sno >> $output
            """
	}
        } else {
	from("Ref_genome.protein_coding.bed","Ref_genome.noncoding.bed","Ref_genome.miRNA.bed","Ref_genome.snoRNA.bed","Rel_ref_genome.protein_coding.bed","Rel_ref_genome.noncoding.bed","Rel_ref_genome.miRNA.bed","Rel_ref_genome.snoRNA.bed",org_name+"to"+rel_sp_name+".over.chain.gz") produce("annotation.config"){
            exec """
	    echo '>'$org_name >> $output ;
            echo 'CODING='$input1 >> $output ;
            echo 'GENOME_FA='$genome >> $output ;
            echo 'ORTHOLOG='$rel_sp_name >> $output ;
            echo 'LIFTOVER='$input9 >> $output ;
            echo 'NONCODING='$input2 >> $output ;
            echo 'MIRNA='$input3 >> $output ;
            echo 'SNORNA='$input4 >> $output ;
            echo '>'$rel_sp_name >> $output ;
            echo 'CODING='$input5 >> $output ;
            echo 'GENOME_FA='$genome_related_species >> $output ;
            echo 'ORTHOLOG='$org_name >> $output ;
            echo 'NONCODING='$input6 >> $output ;
            echo 'MIRNA='$input7 >> $output ;
            echo 'SNORNA='$input8 >> $output
	"""
        }
	}
}

putative_lnc_npcts_bed = {
	output.dir=slncky_dir
	from("Putative.lnc_NPCTs.gtf") produce("Putative-lnc-nptcs.bed"){
	exec """
	$gffread $input --bed -o ${output.dir}/temp.bed ;
	cut -f1-12 ${output.dir}/temp.bed > $output
	"""
	  }
}

run_slncky = {
	output.dir=slncky_dir
	from("annotation.config","Putative-lnc-nptcs.bed") produce("slncky_out.lncs.info.txt"){
	exec """
	source $Activate slncky ;
	$slncky -n $threads -c $input1 $input2 $org_name $slncky_options $output.prefix.prefix.prefix
	"""
	  }
}

ortholog_search = {
	output.dir = slncky_dir
	from("annotation.config", "Putative-lnc-nptcs.bed") produce(rel_sp_name+".orthologs.top.txt"){
        exec """
        source ${Activate} slncky
        $slncky -n $threads -c $input1 $input2 $slncky_ortho_options $org_name $output.prefix.prefix.prefix
        """
    	}
}

slncky_run = segment { prepare_annotations_bed + fasta_index + prepare_Liftover + annotation_config + putative_lnc_npcts_bed + run_slncky + ortholog_search }
