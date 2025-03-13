package com.example.baitapvenha

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.baitapvenha.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    var kq:Double=0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Đăng ký sự kiện click cho tất cả các nút số và phép tính
        val buttons = listOf(
            binding.so0, binding.so1, binding.so2, binding.so3, binding.so4,
            binding.so5, binding.so6, binding.so7, binding.so8, binding.so9,
            binding.cong, binding.tru, binding.nhan, binding.chia,
            binding.C, binding.bang,binding.CE,binding.BS,binding.doidau,binding.daucham
        )
        buttons.forEach { it.setOnClickListener(this) }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.so0.id -> handleInput("0")
            binding.so1.id -> handleInput("1")
            binding.so2.id -> handleInput("2")
            binding.so3.id -> handleInput("3")
            binding.so4.id -> handleInput("4")
            binding.so5.id -> handleInput("5")
            binding.so6.id -> handleInput("6")
            binding.so7.id -> handleInput("7")
            binding.so8.id -> handleInput("8")
            binding.so9.id -> handleInput("9")
            binding.nhan.id -> handleInput("x")
            binding.chia.id -> handleInput("/")
            binding.cong.id -> handleInput("+")
            binding.tru.id -> handleInput("-")
            binding.C.id -> clearInput()
            binding.bang.id -> calculatorResult()
            binding.BS.id->clearElement()
            binding.CE.id->clearNumber()
            binding.doidau.id -> changeSign()
            binding.daucham.id -> handleInput(".")

        }
    }


    //xóa số cuối
    fun clearElement(){
        var rs=binding.ketqua.text.toString()
        if(rs.length!=1){
            val rs_new=rs.dropLast(1)
            binding.ketqua.text=rs_new
        }
        else{
            binding.ketqua.text="0"
        }
        binding.viewHint.text=""
    }

    //xóa số vừa nhập
    fun clearNumber(){

        binding.viewHint.text=""
        var rs=binding.ketqua.text.toString()
        var chuoi:String=""

        for(i in rs.length-1 downTo  0){
            if(rs[rs.length-1]=='x' || rs[rs.length-1]=='-'
                || rs[rs.length-1]=='+'||rs[rs.length-1]=='/' ){
                clearElement()
                return
            }
            else{
                if(rs[i]!='x'&& rs[i]!='+'&& rs[i]!='/'&& rs[i]!='-'){
                    chuoi+=rs[i];
                }
                else{
                    rs=rs.dropLast(chuoi.length)
                    binding.ketqua.text=rs
                    return
                }
            }
        }

        if(rs[rs.length-1]!='x'&& rs[rs.length-1]!='+'&& rs[rs.length-1]!='/'&& rs[rs.length-1]!='-'){
            clearInput()
            return
        }
    }

    //nhập
    fun handleInput(value: String) {
        val size_t=binding.ketqua.text.toString().length
        val chuoi_hientai=binding.ketqua.text.toString()
        if(chuoi_hientai[size_t-1]=='x'|| chuoi_hientai[size_t-1]=='+'
            || chuoi_hientai[size_t-1]=='/'|| chuoi_hientai[size_t-1]=='-'|| chuoi_hientai[size_t-1]=='.'){
            if(value=="x" || value=="/"|| value=="+"|| value=="-" || value=="."){
                return
            }
        }
        if (size_t > 2 && chuoi_hientai[size_t - 1] == '0') {
            val kyTuThuHai = chuoi_hientai[size_t - 2]
            if (kyTuThuHai  == 'x' || kyTuThuHai  == '+' || kyTuThuHai  == '/' || kyTuThuHai  == '-') {
                if (value =="0") {
                   return
                }
                else if(value=="."){
                    binding.ketqua.text=chuoi_hientai
                }
                else{
                    var chuoi=""
                    for(i in 0..size_t-2){
                        chuoi+=chuoi_hientai[i]
                    }
                    binding.ketqua.text=chuoi
                }
            }
        }
        if (binding.ketqua.text.toString() == "0") {
            if (value in "1".."9") {
               binding.ketqua.text=value
            }
            if(value=="x" || value=="/"){
                binding.ketqua.append(value)
            }
            if(value=="+" || value=="-" || value=="."){
                binding.ketqua.append(value)
            }

        }
        else {
            binding.ketqua.append(value)  // Nếu khác "0", nối thêm vào chuỗi hiện tại.
        }
    }



   //xóa hết
    private fun clearInput() {
        binding.ketqua.text = "0"
       binding.viewHint.text=""
    }

    //kết quả
    private fun calculatorResult() {
        var rs:String=binding.ketqua.text.toString()
        val evaluator=Calculator();
        kq=evaluator.evaluate(rs)
        val chuoi=kq.toString()
        var result_end=""

        if(chuoi[chuoi.length-1]=='0' && chuoi[chuoi.length-2]=='.'){
            for(i in 0..chuoi.length-3){
                result_end+=chuoi[i]
            }

            binding.viewHint.text=binding.ketqua.text.toString()
            binding.ketqua.text=result_end
        }
        else{
            binding.ketqua.text=chuoi
            binding.viewHint.text=chuoi
        }
    }

    //đổi dấu
    fun changeSign() {
        var rs = binding.ketqua.text.toString()

        if (rs.isEmpty()) {
            binding.ketqua.text = "0"
            return
        }

        if (rs[0] == '-') {
            val chuoi = rs.substring(1) // Lấy chuỗi từ vị trí thứ 1 đến hết
            binding.ketqua.text = chuoi
            return
        }
        // Trường hợp có ngoặc
        if (rs.endsWith(")")) {
            var first = rs.length - 1
            var last = 0
            var sign = 0
            for (i in rs.length - 3 downTo 0) {
                if (rs[i] == '(') {
                    last = i
                    sign = i + 1
                    break
                }
            }
            rs = rs.removeRange(last, last + 1)  // Xóa '('
            rs = rs.removeRange(first - 1, first) // Xóa ')'
            rs = rs.removeRange(sign - 1, sign)   // Xóa dấu
            binding.ketqua.text = rs
        }
        else {
            var chuoi = ""    // Phần số
            var chuoi_kq = "" // Phần kết quả
            var j = 0

            // Tách số cuối cùng
            for (i in rs.length - 1 downTo 0) {
                if (rs[i] != 'x' && rs[i] != '/' && rs[i] != '+' && rs[i] != '-') {
                    chuoi = rs[i] + chuoi // Đảo ngược để giữ đúng thứ tự
                } else {
                    j = i
                    break
                }
            }
            // Sao chép phần trước số cuối cùng
            for (index in 0 until j) {
                chuoi_kq += rs[index]
            }
            // Xử lý đổi dấu
            when (rs.getOrNull(j)) {
                'x', '/', '+' -> {
                    chuoi_kq += rs[j] + "(-$chuoi)"
                    binding.ketqua.text = chuoi_kq
                }
                '-' -> {
                    binding.ketqua.text = chuoi_kq +rs[j]+ chuoi // Xóa dấu trừ
                }
                else -> {
                    binding.ketqua.text = "(-$rs)" // Trường hợp chỉ có số
                }
            }
        }
    }
}

