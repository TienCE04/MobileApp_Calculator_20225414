package com.example.baitapvenha

import java.util.Stack

class Calculator {
    // Kiểm tra xem ký tự có phải là toán tử không
    private fun isOperator(c: Char): Boolean {
        return c == '+' || c == '-' || c == 'x' || c == '/'
    }

    // Xác định độ ưu tiên của toán tử
    private fun precedence(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            'x', '/' -> 2
            else -> 0
        }
    }

    // Chuyển biểu thức từ Infix sang Postfix
    fun infixToPostfix(expression: String): String {
        val operatorStack = Stack<Char>()
        val postfix = StringBuilder()
        var i = 0

        while (i < expression.length) {
            val current = expression[i]

            // Nếu là số (có thể âm)
            if (current.isDigit() || (current == '-' && (i == 0 || expression[i - 1] in listOf('+', '-', 'x', '/', '(')))) {
                var numStr = ""
                while (i < expression.length && (expression[i].isDigit() || expression[i] == '.' || (expression[i] == '-' && numStr.isEmpty()))) {
                    numStr += expression[i]
                    i++
                }
                postfix.append(numStr).append(' ')
                i--
            } else if (current == '(') { // Xử lý dấu ngoặc mở
                operatorStack.push(current)
            } else if (current == ')') { // Xử lý dấu ngoặc đóng
                while (operatorStack.isNotEmpty() && operatorStack.peek() != '(') {
                    postfix.append(operatorStack.pop()).append(' ')
                }
                if (operatorStack.isNotEmpty()) operatorStack.pop() // Loại bỏ '('
                else throw IllegalArgumentException("Dấu ngoặc không khớp")
            } else if (isOperator(current)) { // Nếu là toán tử
                while (operatorStack.isNotEmpty() && operatorStack.peek() != '(' && precedence(operatorStack.peek()) >= precedence(current)) {
                    postfix.append(operatorStack.pop()).append(' ')
                }
                operatorStack.push(current)
            }
            i++
        }

        // Lấy các toán tử còn lại
        while (operatorStack.isNotEmpty()) {
            if (operatorStack.peek() == '(') throw IllegalArgumentException("Dấu ngoặc không khớp")
            postfix.append(operatorStack.pop()).append(' ')
        }

        return postfix.toString().trim()
    }

    // Tính toán biểu thức hậu tố
    fun evaluatePostfix(postfix: String): Double {
        val operandStack = Stack<Double>()
        val tokens = postfix.split(" ")

        for (token in tokens) {
            if (token.isNotEmpty()) {
                if (token.toDoubleOrNull() != null) { // Nếu là số (bao gồm số thực và số âm)
                    operandStack.push(token.toDouble())
                } else if (isOperator(token[0])) { // Nếu là toán tử
                    val right = operandStack.pop()
                    val left = operandStack.pop()
                    val result = when (token[0]) {
                        '+' -> left + right
                        '-' -> left - right
                        'x' -> left * right
                        '/' -> if (right != 0.0) left / right else throw ArithmeticException("Chia cho 0")
                        else -> throw IllegalArgumentException("Toán tử không hợp lệ: ${token[0]}")
                    }
                    operandStack.push(result)
                }
            }
        }

        return operandStack.pop()
    }

    // Hàm tính toán tổng quát từ Infix
    fun evaluate(expression: String): Double {
        val cleanedExpression = expression.replace("\\s+".toRegex(), "") // Xóa khoảng trắng
        val postfix = infixToPostfix(cleanedExpression)
        println("Postfix: $postfix") // In ra để kiểm tra
        val result = evaluatePostfix(postfix)

        // Chuyển kết quả thành chuỗi để kiểm tra số chữ số sau dấu phẩy
        val resultStr = result.toString()
        val decimalPart = if (resultStr.contains(".")) resultStr.substringAfter(".") else ""

        // Nếu không có phần thập phân hoặc phần thập phân <= 8 chữ số, giữ nguyên
        // Nếu phần thập phân > 8 chữ số, làm tròn về 8 chữ số
       if (decimalPart.length > 8) {
            return String.format("%.8f", result).toDouble()
        }
        return resultStr.toDouble()
    }
}