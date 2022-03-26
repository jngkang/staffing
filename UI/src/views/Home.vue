<template>
    <div style="text-align: center;">
        <el-row :gutter="10" style="margin-bottom: 60px">
            <el-col :span="8">
                <el-card style="color: #409EFF">
                    <div><i class="el-icon-user-solid"/> 用户总数</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold">
                        {{ totalEmployee }}
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card style="color: #F56C6C">
                    <div><i class="el-icon-money"/> 部门总量</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold">
                        {{ totalDepartment }}
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card style="color: #67C23A">
                    <div><i class="el-icon-bank-card"/> 岗位总量</div>
                    <div style="padding: 10px 0; text-align: center; font-weight: bold">
                        {{ totalPost }}
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-row :span="24">
            <div id="main" style="height: 550px;"></div>
        </el-row>
    </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
    name: "Home",
    data() {
        return {
            totalEmployee: 0,
            totalDepartment: 0,
            totalPost: 0,
        }
    },
    mounted() {
        // 获取员工的总数
        this.request.get("/employee/count").then(res => {
            this.totalEmployee = res.data;
        });

        // 加载两个饼图的形状及数据
        var chartDom = document.getElementById('main');
        var myChart = echarts.init(chartDom);
        var option;

        option = {
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            series: [
                {
                    name: '部门总人数',
                    type: 'pie',
                    radius: [20, 140],
                    center: ['25%', '50%'],
                    roseType: 'radius',
                    itemStyle: {
                        borderRadius: 5
                    },
                    data: [],
                },
                {
                    name: '岗位总人数',
                    type: 'pie',
                    radius: [20, 140],
                    center: ['75%', '50%'],
                    roseType: 'radius',
                    itemStyle: {
                        borderRadius: 5
                    },
                    data: [],
                },

            ]
        };

        option && myChart.setOption(option);

        // 获取各个部门的人员信息
        this.request.get("/employee/deptcount").then(res => {
            option.series[0].data = res.data;
            res.data.forEach(r => {
                this.totalDepartment += r.value;
            })
            myChart.setOption(option);
        });

        // 获取各个岗位的人员信息
        this.request.get("/employee/postcount").then(res => {
            option.series[1].data = res.data;
            res.data.forEach(r => {
                this.totalPost += r.value;
            })
            myChart.setOption(option);
        });

    }
}
</script>

<style scoped>

</style>
